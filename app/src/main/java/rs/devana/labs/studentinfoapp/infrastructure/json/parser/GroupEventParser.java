package rs.devana.labs.studentinfoapp.infrastructure.json.parser;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.inject.Inject;

import rs.devana.labs.studentinfoapp.domain.models.event.GroupEvent.GroupEvent;

public class GroupEventParser {
    ClassroomParser classroomParser;
    NotificationParser notificationParser;
    GroupParser groupParser;

    @Inject
    public GroupEventParser(ClassroomParser classroomParser, NotificationParser notificationParser, GroupParser groupParser) {
        this.classroomParser = classroomParser;
        this.notificationParser = notificationParser;
        this.groupParser = groupParser;
    }

    public GroupEvent parse(JSONObject jsonGroupEvent) {
        try {
            String startsAt = jsonGroupEvent.getJSONObject("datetime").getString("startsAt").substring(0, 19) + ".000-" + jsonGroupEvent.getJSONObject("datetime").getString("startsAt").substring(20, 24);
            String endsAt = jsonGroupEvent.getJSONObject("datetime").getString("endsAt").substring(0, 19) + ".000-" + jsonGroupEvent.getJSONObject("datetime").getString("endsAt").substring(20, 24);

            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

            Calendar startsAtCalendar = Calendar.getInstance();
            startsAtCalendar.setTime(df.parse(startsAt));

            Calendar endsAtCalendar = Calendar.getInstance();
            endsAtCalendar.setTime(df.parse(endsAt));

            return new GroupEvent(jsonGroupEvent.getInt("id"), jsonGroupEvent.getString("type"), jsonGroupEvent.getString("description"), groupParser.parse(jsonGroupEvent.getJSONObject("course")), startsAtCalendar, endsAtCalendar, classroomParser.parse(jsonGroupEvent.getJSONArray("classrooms")), notificationParser.parse(jsonGroupEvent.getJSONArray("notifications")));
        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
