package rs.devana.labs.studentinfo.infrastructure.repository;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import javax.inject.Inject;

import rs.devana.labs.studentinfo.domain.models.classroom.Classroom;
import rs.devana.labs.studentinfo.domain.models.classroom.ClassroomRepositoryInterface;
import rs.devana.labs.studentinfo.infrastructure.json.parser.ClassroomParser;

public class ClassroomRepository implements ClassroomRepositoryInterface {
    ClassroomParser classroomParser;

    @Inject
    public ClassroomRepository(ClassroomParser classroomParser){
        this.classroomParser = classroomParser;
    }

    @Override
    public List<Classroom> getClassrooms(JSONArray jsonClassroom) {
        return null;
    }

    @Override
    public Classroom getClassroom(JSONObject jsonClassroom) {
        return classroomParser.parse(jsonClassroom);
    }
}
