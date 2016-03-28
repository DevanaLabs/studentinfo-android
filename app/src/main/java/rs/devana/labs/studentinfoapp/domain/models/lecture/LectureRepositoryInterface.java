package rs.devana.labs.studentinfoapp.domain.models.lecture;


import java.util.List;

public interface LectureRepositoryInterface {
    List<Lecture> getAllLecturesForGroup(int groupId);
}
