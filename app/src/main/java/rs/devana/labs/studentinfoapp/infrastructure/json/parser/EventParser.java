package rs.devana.labs.studentinfoapp.infrastructure.json.parser;

import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rs.devana.labs.studentinfoapp.domain.models.event.CourseEvent.CourseEvent;
import rs.devana.labs.studentinfoapp.domain.models.event.Event;
import rs.devana.labs.studentinfoapp.domain.models.event.GlobalEvent.GlobalEvent;
import rs.devana.labs.studentinfoapp.domain.models.event.GroupEvent.GroupEvent;

public class EventParser {

    GroupEventParser groupEventParser;
    GlobalEventParser globalEventParser;
    CourseEventParser courseEventParser;
    SharedPreferences sharedPreferences;

    @Inject
    public EventParser(GroupEventParser groupEventParser, GlobalEventParser globalEventParser, CourseEventParser courseEventParser, SharedPreferences sharedPreferences) {
        this.groupEventParser = groupEventParser;
        this.globalEventParser = globalEventParser;
        this.courseEventParser = courseEventParser;
        this.sharedPreferences = sharedPreferences;
    }

    public Event parse(JSONObject jsonEvent) {
        if (jsonEvent.has("group")) {
            return groupEventParser.parse(jsonEvent);
        } else if (jsonEvent.has("course")) {
            return courseEventParser.parse(jsonEvent);
        } else {
            return globalEventParser.parse(jsonEvent);
        }
    }

    public List<Event> parse(JSONArray jsonEvents) {
        List<Event> events = new ArrayList<>();

        String groupId = sharedPreferences.getString("groupId", "");
        for (int i = 0; i < jsonEvents.length(); i++) {
            try {
                if ((jsonEvents.getJSONObject(i).has("group")) && (jsonEvents.getJSONObject(i).getString("groupId").equals(groupId))) {
                    GroupEvent globalEvent = groupEventParser.parse(jsonEvents.getJSONObject(i));
                    events.add(globalEvent);
                } else if (jsonEvents.getJSONObject(i).has("course")) {
                    JSONArray jsonLectures = new JSONArray(sharedPreferences.getString("lectures", ""));
                    for (int j = 0; j < jsonLectures.length(); j++) {
                        if (jsonLectures.getJSONObject(j).getJSONObject("course").getString("name").equals(jsonEvents.getJSONObject(i).getJSONObject("course").getString("name"))) {
                            CourseEvent courseEvent = courseEventParser.parse(jsonEvents.getJSONObject(i));
                            events.add(courseEvent);
                            break;
                        }
                    }
                } else {
                    GlobalEvent globalEvent = globalEventParser.parse(jsonEvents.getJSONObject(i));
                    events.add(globalEvent);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return events;
    }

    public Event getEvent(int id) {
        try {
            JSONArray jsonEvent = new JSONArray(sharedPreferences.getString("allEvents", "[]"));
            for (int i = 0; i < jsonEvent.length(); i++) {
                if (jsonEvent.getJSONObject(i).getInt("id") == id) {
                    return parse(jsonEvent.getJSONObject(i));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addNotificationToEvent(JSONObject jsonNotification) {
        try {
            JSONArray jsonEvents = new JSONArray(sharedPreferences.getString("allEvents", "[]"));

            for (int i = 0; i < jsonEvents.length(); i++) {
                if (jsonEvents.getJSONObject(i).getInt("id") == jsonNotification.getJSONObject("event").getInt("id")) {
                    jsonEvents.getJSONObject(i).getJSONArray("notifications").put(jsonNotification);
                }
            }

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("allEvents", jsonEvents.toString());
            editor.apply();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
