package rs.devana.labs.studentinfo.infrastructure.json.parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rs.devana.labs.studentinfo.domain.models.lecture.Lecture;

public class LectureParser {
    CourseParser courseParser;
    ClassroomParser classroomParser;
    LectureNotificationParser lectureNotificationParser;
     private static final int secondsInDay = 86400;

    @Inject
    public LectureParser(CourseParser courseParser, ClassroomParser classroomParser, LectureNotificationParser lectureNotificationParser){
        this.courseParser = courseParser;
        this.classroomParser = classroomParser;
        this.lectureNotificationParser = lectureNotificationParser;
    }

    public Lecture parse(JSONObject jsonLecture){
        try {
            return new Lecture(jsonLecture.getInt("id"), jsonLecture.getString("type"), courseParser.parse(jsonLecture.getJSONObject("course")), jsonLecture.getJSONObject("time").getInt("startsAt"), jsonLecture.getJSONObject("time").getInt("endsAt"), jsonLecture.getJSONObject("teacher").getString("firstName"), classroomParser.parse(jsonLecture.getJSONObject("classroom")), lectureNotificationParser.parse(jsonLecture.getJSONArray("notifications")));
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

                Lecture lecture = new Lecture(jsonLecture.getInt("id"), jsonLecture.getString("type"), courseParser.parse(jsonLecture.getJSONObject("course")), jsonLecture.getJSONObject("time").getInt("startsAt"), jsonLecture.getJSONObject("time").getInt("endsAt"), jsonLecture.getJSONObject("teacher").getString("firstName"), classroomParser.parse(jsonLecture.getJSONObject("classroom")), lectureNotificationParser.parse(jsonLecture.getJSONArray("notifications")));

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
                    Lecture lecture = new Lecture(jsonLecture.getInt("id"), jsonLecture.getString("type"), courseParser.parse(jsonLecture.getJSONObject("course")), jsonLecture.getJSONObject("time").getInt("startsAt"), jsonLecture.getJSONObject("time").getInt("endsAt"), jsonLecture.getJSONObject("teacher").getString("firstName"), classroomParser.parse(jsonLecture.getJSONObject("classroom")), lectureNotificationParser.parse(jsonLecture.getJSONArray("notifications")));

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
}
