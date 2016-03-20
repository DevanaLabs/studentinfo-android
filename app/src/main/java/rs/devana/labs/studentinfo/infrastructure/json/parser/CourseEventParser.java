package rs.devana.labs.studentinfo.infrastructure.json.parser;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.inject.Inject;

import rs.devana.labs.studentinfo.domain.models.event.CourseEvent.CourseEvent;

public class CourseEventParser {
    ClassroomParser classroomParser;
    NotificationParser notificationParser;
    CourseParser courseParser;

    @Inject
    public CourseEventParser(ClassroomParser classroomParser, NotificationParser notificationParser, CourseParser courseParser) {
        this.classroomParser = classroomParser;
        this.notificationParser = notificationParser;
        this.courseParser = courseParser;
    }

    public CourseEvent parse(JSONObject jsonCourseEvent) {
        try {
            String startsAt = jsonCourseEvent.getJSONObject("datetime").getString("startsAt").substring(0, 19) + ".000-" + jsonCourseEvent.getJSONObject("datetime").getString("startsAt").substring(20, 24);
            String endsAt = jsonCourseEvent.getJSONObject("datetime").getString("endsAt").substring(0, 19) + ".000-" + jsonCourseEvent.getJSONObject("datetime").getString("endsAt").substring(20, 24);

            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

            Calendar startsAtCalendar = Calendar.getInstance();
            startsAtCalendar.setTime(df.parse(startsAt));

            Calendar endsAtCalendar = Calendar.getInstance();
            endsAtCalendar.setTime(df.parse(endsAt));

            return new CourseEvent(jsonCourseEvent.getInt("id"), jsonCourseEvent.getString("type"), jsonCourseEvent.getString("description"), courseParser.parse(jsonCourseEvent.getJSONObject("course")), startsAtCalendar, endsAtCalendar, classroomParser.parse(jsonCourseEvent.getJSONArray("classrooms")), notificationParser.parse(jsonCourseEvent.getJSONArray("notifications")));
        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
