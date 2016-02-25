package rs.devana.labs.studentinfo.infrastructure.dagger.module;

import dagger.Module;
import dagger.Provides;
import rs.devana.labs.studentinfo.domain.api.ApiDataFetch;
import rs.devana.labs.studentinfo.domain.models.lecture.LectureRepositoryInterface;
import rs.devana.labs.studentinfo.infrastructure.repository.ClassroomRepository;
import rs.devana.labs.studentinfo.infrastructure.repository.CourseRepository;
import rs.devana.labs.studentinfo.infrastructure.repository.LectureNotificationRepository;
import rs.devana.labs.studentinfo.infrastructure.repository.LectureRepository;

@Module
public class LectureRepositoryModule {

    @Provides
    public LectureRepositoryInterface ProvideLectureRepository(ApiDataFetch apiDataFetch, CourseRepository courseRepository, ClassroomRepository classroomRepository, LectureNotificationRepository lectureNotificationRepository){
        return new LectureRepository(apiDataFetch, courseRepository, classroomRepository, lectureNotificationRepository);
    }
}
