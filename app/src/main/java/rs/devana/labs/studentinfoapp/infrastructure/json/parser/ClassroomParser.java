package rs.devana.labs.studentinfoapp.infrastructure.json.parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rs.devana.labs.studentinfoapp.domain.models.classroom.Classroom;

public class ClassroomParser {

    @Inject
    public ClassroomParser() {
    }

    public Classroom parse(JSONObject jsonClassroom) {
        try {
            return new Classroom(jsonClassroom.getInt("id"), jsonClassroom.getString("name"), jsonClassroom.getInt("floor"), null);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  null;
    }

    public List<Classroom> parse(JSONArray jsonClassrooms) {
        List<Classroom> classrooms = new ArrayList<>();

        for (int i = 0; i < jsonClassrooms.length(); i++) {
            try {
                classrooms.add(new Classroom(jsonClassrooms.getJSONObject(i).getInt("id"), jsonClassrooms.getJSONObject(i).getString("name"), jsonClassrooms.getJSONObject(i).getInt("floor"), null));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return classrooms;
    }
}
