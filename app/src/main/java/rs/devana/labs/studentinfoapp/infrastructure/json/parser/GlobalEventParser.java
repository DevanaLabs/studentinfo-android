package rs.devana.labs.studentinfoapp.infrastructure.json.parser;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.inject.Inject;

import rs.devana.labs.studentinfoapp.domain.models.event.GlobalEvent.GlobalEvent;


public class GlobalEventParser {
    ClassroomParser classroomParser;
    NotificationParser notificationParser;

    @Inject
    public GlobalEventParser(ClassroomParser classroomParser, NotificationParser notificationParser) {
        this.classroomParser = classroomParser;
        this.notificationParser = notificationParser;
    }

    public GlobalEvent parse(JSONObject jsonGlobalEvent) {
        try {
            String startsAt = jsonGlobalEvent.getJSONObject("datetime").getString("startsAt").substring(0, 19) + ".000-" + jsonGlobalEvent.getJSONObject("datetime").getString("startsAt").substring(20, 24);
            String endsAt = jsonGlobalEvent.getJSONObject("datetime").getString("endsAt").substring(0, 19) + ".000-" + jsonGlobalEvent.getJSONObject("datetime").getString("endsAt").substring(20, 24);

            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

            Calendar startsAtCalendar = Calendar.getInstance();
            startsAtCalendar.setTime(df.parse(startsAt));

            Calendar endsAtCalendar = Calendar.getInstance();
            endsAtCalendar.setTime(df.parse(endsAt));

            return new GlobalEvent(jsonGlobalEvent.getInt("id"), jsonGlobalEvent.getString("type"), jsonGlobalEvent.getString("description"), startsAtCalendar, endsAtCalendar, classroomParser.parse(jsonGlobalEvent.getJSONArray("classrooms")), notificationParser.parse(jsonGlobalEvent.getJSONArray("notifications")));
        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
