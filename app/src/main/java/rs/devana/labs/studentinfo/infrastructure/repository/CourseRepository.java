package rs.devana.labs.studentinfo.infrastructure.repository;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import javax.inject.Inject;

import rs.devana.labs.studentinfo.domain.models.course.Course;
import rs.devana.labs.studentinfo.domain.models.course.CourseRepositoryInterface;

public class CourseRepository implements CourseRepositoryInterface {
    @Inject
    public CourseRepository(){
    }

    @Override
    public List<Course> getAllCourses() {
        return null;
    }

    @Override
    public Course getCourse(JSONObject jsonCourse) {
        try {
            return new Course(jsonCourse.getInt("id"), jsonCourse.getString("name"), jsonCourse.getInt("semester"), jsonCourse.getInt("espb"), null);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
