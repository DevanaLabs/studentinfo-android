package rs.devana.labs.studentinfo.infrastructure.repository;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rs.devana.labs.studentinfo.domain.api.ApiDataFetch;
import rs.devana.labs.studentinfo.domain.models.lecture.Lecture;
import rs.devana.labs.studentinfo.domain.models.lecture.LectureRepositoryInterface;

public class LectureRepository implements LectureRepositoryInterface {
    @Inject
    ApiDataFetch apiDataFetch;
    @Override
    public List<Lecture> getAllLecturesForGroup(int groupId) {
        JSONArray jsonLectures = apiDataFetch.getLecturesForGroup(groupId);
        List<Lecture> lectureList = new ArrayList<>();
        //TODO: transform jsonArray of lectures into List of lectures
        return lectureList;
    }
}
