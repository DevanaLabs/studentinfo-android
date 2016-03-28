package rs.devana.labs.studentinfoapp.infrastructure.repository;

import org.json.JSONArray;

import java.util.List;

import javax.inject.Inject;

import rs.devana.labs.studentinfoapp.domain.api.ApiDataFetch;
import rs.devana.labs.studentinfoapp.domain.models.lecture.Lecture;
import rs.devana.labs.studentinfoapp.domain.models.lecture.LectureRepositoryInterface;
import rs.devana.labs.studentinfoapp.infrastructure.json.parser.LectureParser;

public class LectureRepository implements LectureRepositoryInterface {
    ApiDataFetch apiDataFetch;
    LectureParser lectureParser;

    @Inject
    public LectureRepository(ApiDataFetch apiDataFetch, LectureParser lectureParser){
        this.apiDataFetch = apiDataFetch;
        this.lectureParser = lectureParser;
    }

    @Override
    public List<Lecture> getAllLecturesForGroup(int groupId) {
        JSONArray jsonLectures = apiDataFetch.getLecturesForGroup(groupId);

        return lectureParser.parse(jsonLectures);
    }
}
