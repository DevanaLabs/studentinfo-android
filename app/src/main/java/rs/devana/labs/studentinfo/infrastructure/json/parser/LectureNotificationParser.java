package rs.devana.labs.studentinfo.infrastructure.json.parser;

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

import rs.devana.labs.studentinfo.domain.models.notification.lecture.LectureNotification;

public class LectureNotificationParser {
    LectureParser lectureParser;

    @Inject LectureNotificationParser(LectureParser lectureParser){
        this.lectureParser = lectureParser;
    }
    public LectureNotification parse(JSONObject jsonLectureNotification){
        try {
            String stringNotification = jsonLectureNotification.getString("expiresAt").substring(0, 19) + ".000-"+ jsonLectureNotification.getString("expiresAt").substring(20, 24);

            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(df.parse(stringNotification));

            return new LectureNotification(jsonLectureNotification.getInt("id"), jsonLectureNotification.getString("description"), calendar, lectureParser.parse(jsonLectureNotification.getJSONObject("lecture")));
        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<LectureNotification> parse(JSONArray jsonLectureNotifications){
        List<LectureNotification> lectureNotifications = new ArrayList<>();

        int i = 0;
        while(i < jsonLectureNotifications.length()){
            try {
                JSONObject jsonLectureNotification = jsonLectureNotifications.getJSONObject(i);

                String stringNotification = jsonLectureNotification.getString("expiresAt").substring(0, 19) + ".000-"+ jsonLectureNotification.getString("expiresAt").substring(20, 24);

                DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(df.parse(stringNotification));

                LectureNotification lectureNotification = new LectureNotification(jsonLectureNotification.getInt("id"), jsonLectureNotification.getString("description"), calendar, lectureParser.parse(jsonLectureNotification.getJSONObject("lecture")));
                lectureNotifications.add(lectureNotification);
            } catch (JSONException | ParseException e) {
                e.printStackTrace();
            }
        }
        return lectureNotifications;
    }
}
