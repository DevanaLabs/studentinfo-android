package rs.devana.labs.studentinfo.infrastructure.json.parser;

import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rs.devana.labs.studentinfo.domain.models.event.CourseEvent.CourseEvent;
import rs.devana.labs.studentinfo.domain.models.event.Event;
import rs.devana.labs.studentinfo.domain.models.event.GlobalEvent.GlobalEvent;
import rs.devana.labs.studentinfo.domain.models.event.GroupEvent.GroupEvent;

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

    public Event parse(JSONObject jsonObject){
        return null;
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
}
