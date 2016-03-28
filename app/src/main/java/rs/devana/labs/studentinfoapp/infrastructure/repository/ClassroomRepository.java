package rs.devana.labs.studentinfoapp.infrastructure.repository;

import javax.inject.Inject;

import rs.devana.labs.studentinfoapp.domain.models.classroom.ClassroomRepositoryInterface;
import rs.devana.labs.studentinfoapp.infrastructure.json.parser.ClassroomParser;

public class ClassroomRepository implements ClassroomRepositoryInterface {
    ClassroomParser classroomParser;

    @Inject
    public ClassroomRepository(ClassroomParser classroomParser){
        this.classroomParser = classroomParser;
    }
}
