package rs.devana.labs.studentinfoapp.infrastructure.json.parser;

import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rs.devana.labs.studentinfoapp.domain.models.lecture.Lecture;

public class LectureParser {
    SharedPreferences sharedPreferences;
    CourseParser courseParser;
    ClassroomParser classroomParser;
    LectureNotificationParser lectureNotificationParser;
    private static final int secondsInDay = 86400;

    @Inject
    public LectureParser(CourseParser courseParser, ClassroomParser classroomParser, LectureNotificationParser lectureNotificationParser, SharedPreferences sharedPreferences) {
        this.courseParser = courseParser;
        this.classroomParser = classroomParser;
        this.lectureNotificationParser = lectureNotificationParser;
        this.sharedPreferences = sharedPreferences;
    }

    public Lecture parse(JSONObject jsonLecture){
        try {
            return new Lecture(jsonLecture.getInt("id"), jsonLecture.getInt("type"), courseParser.parse(jsonLecture.getJSONObject("course")), jsonLecture.getJSONObject("time").getInt("startsAt"), jsonLecture.getJSONObject("time").getInt("endsAt"), jsonLecture.getJSONObject("teacher").getString("firstName") + " " + jsonLecture.getJSONObject("teacher").getString("lastName"), classroomParser.parse(jsonLecture.getJSONObject("classroom")), lectureNotificationParser.parse(jsonLecture.getJSONArray("notifications")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Lecture> parse(JSONArray jsonLectures){

        List<Lecture> lectures = new ArrayList<>();

        int i = 0;
        while (i < jsonLectures.length()) {
            try {
                JSONObject jsonLecture = jsonLectures.getJSONObject(i);

                Lecture lecture = new Lecture(jsonLecture.getInt("id"), jsonLecture.getInt("type"), courseParser.parse(jsonLecture.getJSONObject("course")), jsonLecture.getJSONObject("time").getInt("startsAt"), jsonLecture.getJSONObject("time").getInt("endsAt"), jsonLecture.getJSONObject("teacher").getString("firstName") + " " + jsonLecture.getJSONObject("teacher").getString("lastName"), classroomParser.parse(jsonLecture.getJSONObject("classroom")), lectureNotificationParser.parse(jsonLecture.getJSONArray("notifications")));

                lectures.add(lecture);
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                i++;
            }
        }
        return lectures;
    }

    public List<Lecture> getLecturesForDay(int day, JSONArray jsonLectures){

        List<Lecture> lectures = new ArrayList<>();

        int i = 0;
        while (i < jsonLectures.length()) {
            try {
                JSONObject jsonLecture = jsonLectures.getJSONObject(i);
                if ((day*secondsInDay < jsonLecture.getJSONObject("time").getInt("startsAt") && jsonLecture.getJSONObject("time").getInt("startsAt") < (day+1)*secondsInDay)) {
                    Lecture lecture = new Lecture(jsonLecture.getInt("id"), jsonLecture.getInt("type"), courseParser.parse(jsonLecture.getJSONObject("course")), jsonLecture.getJSONObject("time").getInt("startsAt"), jsonLecture.getJSONObject("time").getInt("endsAt"), jsonLecture.getJSONObject("teacher").getString("firstName") + " " + jsonLecture.getJSONObject("teacher").getString("lastName"), classroomParser.parse(jsonLecture.getJSONObject("classroom")), lectureNotificationParser.parse(jsonLecture.getJSONArray("notifications")));

                    lectures.add(lecture);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                i++;
            }
        }
        return lectures;
    }

    public Lecture getLecture(int id) {
        try {
            JSONArray jsonLectures = new JSONArray(sharedPreferences.getString("lectures", "[]"));
            for (int i = 0; i < jsonLectures.length(); i++) {
                if (jsonLectures.getJSONObject(i).getInt("id") == id) {
                    return parse(jsonLectures.getJSONObject(i));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addNotificationToLecture(JSONObject jsonNotification) {
        try {
            JSONArray jsonLectures = new JSONArray(sharedPreferences.getString("lectures", "[]"));

            for (int i = 0; i < jsonLectures.length(); i++) {
                if (jsonLectures.getJSONObject(i).getInt("id") == jsonNotification.getJSONObject("lecture").getInt("id")) {
                    jsonLectures.getJSONObject(i).getJSONArray("notifications").put(jsonNotification);
                }
            }

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("lectures", jsonLectures.toString());
            editor.apply();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
