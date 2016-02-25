package rs.devana.labs.studentinfo.infrastructure.repository;

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

import rs.devana.labs.studentinfo.domain.api.ApiDataFetch;
import rs.devana.labs.studentinfo.domain.models.lecture.Lecture;
import rs.devana.labs.studentinfo.domain.models.lecture.LectureRepositoryInterface;

public class LectureRepository implements LectureRepositoryInterface {
    ApiDataFetch apiDataFetch;
    CourseRepository courseRepository;
    ClassroomRepository classroomRepository;
    LectureNotificationRepository lectureNotificationRepository;

    @Inject
    public LectureRepository(ApiDataFetch apiDataFetch, CourseRepository courseRepository, ClassroomRepository classroomRepository, LectureNotificationRepository lectureNotificationRepository){
        this.apiDataFetch = apiDataFetch;
        this.courseRepository = courseRepository;
        this.classroomRepository = classroomRepository;
        this.lectureNotificationRepository = lectureNotificationRepository;
    }

    @Override
    public List<Lecture> getAllLecturesForGroup(int groupId) {
        JSONArray jsonLectures = apiDataFetch.getLecturesForGroup(groupId);

        List<Lecture> lectures = new ArrayList<>();

        int i = 0;
        while (i < jsonLectures.length()) {
            try {
                JSONObject jsonLecture = jsonLectures.getJSONObject(i);

                Lecture lecture = new Lecture(jsonLecture.getInt("id"), jsonLecture.getString("type"), courseRepository.getCourse(jsonLecture.getJSONObject("course")), jsonLecture.getJSONObject("time").getInt("startsAt"), jsonLecture.getJSONObject("time").getInt("endsAt"), jsonLecture.getJSONObject("teacher").getString("firstName"), classroomRepository.getClassroom(jsonLecture.getJSONObject("classroom")), lectureNotificationRepository.getLectureNotifications(jsonLecture.getJSONArray("notifications")));

                lectures.add(lecture);
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                i++;
            }
        }
        return lectures;
    }

    @Override
    public List<Lecture> getLectures(JSONArray jsonLectures) {
        return null;
    }
}
