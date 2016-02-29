package rs.devana.labs.studentinfo.infrastructure.json.parser;

import org.json.JSONException;
import org.json.JSONObject;

import rs.devana.labs.studentinfo.domain.models.classroom.Classroom;

public class ClassroomParser {
    public Classroom parse(JSONObject jsonClassroom) {
        try {
            return new Classroom(jsonClassroom.getInt("id"), jsonClassroom.getString("name"), jsonClassroom.getInt("floor"), null);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  null;
    }
}
