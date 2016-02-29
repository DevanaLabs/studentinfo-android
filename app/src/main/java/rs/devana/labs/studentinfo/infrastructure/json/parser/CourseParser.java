package rs.devana.labs.studentinfo.infrastructure.json.parser;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

import rs.devana.labs.studentinfo.domain.models.course.Course;

public class CourseParser {

    @Inject
    public CourseParser() {
    }

    public Course parse(JSONObject jsonCourse) {
        try {
            return new Course(jsonCourse.getInt("id"), jsonCourse.getString("name"), jsonCourse.getInt("semester"), jsonCourse.getInt("espb"), null);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
