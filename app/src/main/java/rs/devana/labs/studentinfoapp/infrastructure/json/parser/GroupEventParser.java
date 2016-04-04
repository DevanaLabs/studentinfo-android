package rs.devana.labs.studentinfoapp.infrastructure.json.parser;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import javax.inject.Inject;

import rs.devana.labs.studentinfoapp.domain.models.event.GroupEvent.GroupEvent;

public class GroupEventParser {
    ClassroomParser classroomParser;
    GroupParser groupParser;
    EventNotificationParser eventNotificationParser;

    @Inject
    public GroupEventParser(ClassroomParser classroomParser, EventNotificationParser eventNotificationParser, GroupParser groupParser) {
        this.classroomParser = classroomParser;
        this.eventNotificationParser = eventNotificationParser;
        this.groupParser = groupParser;
    }

    public GroupEvent parse(JSONObject jsonGroupEvent) {
        try {
            String startsAt = jsonGroupEvent.getJSONObject("datetime").getString("startsAt").substring(0, 19) + ".000+" + jsonGroupEvent.getJSONObject("datetime").getString("startsAt").substring(20, 24);
            String endsAt = jsonGroupEvent.getJSONObject("datetime").getString("endsAt").substring(0, 19) + ".000+" + jsonGroupEvent.getJSONObject("datetime").getString("endsAt").substring(20, 24);

            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault());

            Calendar startsAtCalendar = Calendar.getInstance();
            startsAtCalendar.setTime(df.parse(startsAt));

            Calendar endsAtCalendar = Calendar.getInstance();
            endsAtCalendar.setTime(df.parse(endsAt));

            return new GroupEvent(jsonGroupEvent.getInt("id"), jsonGroupEvent.getString("type"), jsonGroupEvent.getString("description"), groupParser.parse(jsonGroupEvent.getJSONObject("course")), startsAtCalendar, endsAtCalendar, classroomParser.parse(jsonGroupEvent.getJSONArray("classrooms")), eventNotificationParser.parse(jsonGroupEvent.getJSONArray("notifications")));
        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
