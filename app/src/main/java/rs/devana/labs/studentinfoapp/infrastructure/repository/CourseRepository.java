package rs.devana.labs.studentinfoapp.infrastructure.repository;

import java.util.List;

import javax.inject.Inject;

import rs.devana.labs.studentinfoapp.domain.models.course.Course;
import rs.devana.labs.studentinfoapp.domain.models.course.CourseRepositoryInterface;
import rs.devana.labs.studentinfoapp.infrastructure.json.parser.CourseParser;

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
}
