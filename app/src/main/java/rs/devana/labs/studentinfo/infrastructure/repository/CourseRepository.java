package rs.devana.labs.studentinfo.infrastructure.repository;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import javax.inject.Inject;

import rs.devana.labs.studentinfo.domain.models.course.Course;
import rs.devana.labs.studentinfo.domain.models.course.CourseRepositoryInterface;
import rs.devana.labs.studentinfo.infrastructure.json.parser.CourseParser;

public class CourseRepository implements CourseRepositoryInterface {
    CourseParser courseParser;

    @Inject
    public CourseRepository(CourseParser courseParser){
        this.courseParser = courseParser;
    }

    @Override
    public List<Course> getAllCourses() {
        return null;
    }

    @Override
    public Course getCourse(JSONObject jsonCourse) {
        return courseParser.parse(jsonCourse);
    }
}
