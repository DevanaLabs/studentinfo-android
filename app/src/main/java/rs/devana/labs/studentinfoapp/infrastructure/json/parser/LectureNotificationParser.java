package rs.devana.labs.studentinfoapp.infrastructure.json.parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import rs.devana.labs.studentinfoapp.domain.models.notification.lecture.LectureNotification;

public class LectureNotificationParser {

    @Inject
    public LectureNotificationParser() {
    }

    public LectureNotification parse(JSONObject jsonLectureNotification) {
        try {
            String stringNotification = jsonLectureNotification.getString("expiresAt").substring(0, 19) + ".000-" + jsonLectureNotification.getString("expiresAt").substring(20, 24);

            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(df.parse(stringNotification));

            return new LectureNotification(jsonLectureNotification.getInt("id"), jsonLectureNotification.getString("description"), calendar, null);
        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<LectureNotification> parse(JSONArray jsonLectureNotifications) {
        List<LectureNotification> lectureNotifications = new ArrayList<>();

        int i = 0;
        while (i < jsonLectureNotifications.length()) {
            try {
                JSONObject jsonLectureNotification = jsonLectureNotifications.getJSONObject(i);

                String stringNotification = jsonLectureNotification.getString("expiresAt").substring(0, 19) + ".000-" + jsonLectureNotification.getString("expiresAt").substring(20, 24);

                DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(df.parse(stringNotification));

                LectureNotification lectureNotification = new LectureNotification(jsonLectureNotification.getInt("id"), jsonLectureNotification.getString("description"), calendar, null);
                lectureNotifications.add(lectureNotification);
            } catch (JSONException | ParseException e) {
                e.printStackTrace();
            }
        }
        return lectureNotifications;
    }
}
