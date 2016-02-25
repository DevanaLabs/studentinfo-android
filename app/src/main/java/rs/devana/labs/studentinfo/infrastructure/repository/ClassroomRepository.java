package rs.devana.labs.studentinfo.infrastructure.repository;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import javax.inject.Inject;

import rs.devana.labs.studentinfo.domain.models.classroom.Classroom;
import rs.devana.labs.studentinfo.domain.models.classroom.ClassroomRepositoryInterface;

public class ClassroomRepository implements ClassroomRepositoryInterface {
    @Inject
    public ClassroomRepository(){
    }

    @Override
    public List<Classroom> getClassrooms(JSONArray jsonClassroom) {
        return null;
    }

    @Override
    public Classroom getClassroom(JSONObject jsonClassroom) {
        try {
            return new Classroom(jsonClassroom.getInt("id"), jsonClassroom.getString("name"), jsonClassroom.getInt("floor"), null);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  null;
    }
}
