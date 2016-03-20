package rs.devana.labs.studentinfo.infrastructure.json.parser;

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

    @Inject
    public EventParser(GroupEventParser groupEventParser, GlobalEventParser globalEventParser, CourseEventParser courseEventParser) {
        this.groupEventParser = groupEventParser;
        this.globalEventParser = globalEventParser;
        this.courseEventParser = courseEventParser;
    }

    public Event parse(JSONObject jsonEvent){
        return null;
    }

    public List<Event> parse(JSONArray jsonEvents) {
        List<Event> events = new ArrayList<>();

        for (int i = 0; i < jsonEvents.length(); i++) {
            try {
                if (jsonEvents.getJSONObject(i).has("groupId")) {
                    GroupEvent globalEvent = groupEventParser.parse(jsonEvents.getJSONObject(i));
                    events.add(globalEvent);
                } else if (jsonEvents.getJSONObject(i).has("courseId")) {
                    CourseEvent courseEvent = courseEventParser.parse(jsonEvents.getJSONObject(i));
                    events.add(courseEvent);
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
