package rs.devana.labs.studentinfo.domain.models.classroom;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public interface ClassroomRepositoryInterface {
    List<Classroom> getClassrooms(JSONArray jsonClassrooms);

    Classroom getClassroom(JSONObject jsonClassroom);
}
