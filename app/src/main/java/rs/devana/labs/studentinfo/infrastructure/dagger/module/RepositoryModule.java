package rs.devana.labs.studentinfo.infrastructure.dagger.module;

import dagger.Module;
import dagger.Provides;
import rs.devana.labs.studentinfo.domain.api.ApiDataFetch;
import rs.devana.labs.studentinfo.domain.models.lecture.LectureRepositoryInterface;
import rs.devana.labs.studentinfo.domain.models.notification.NotificationRepositoryInterface;
import rs.devana.labs.studentinfo.infrastructure.json.parser.LectureParser;
import rs.devana.labs.studentinfo.infrastructure.json.parser.NotificationParser;
import rs.devana.labs.studentinfo.infrastructure.repository.ClassroomRepository;
import rs.devana.labs.studentinfo.infrastructure.repository.CourseRepository;
import rs.devana.labs.studentinfo.infrastructure.repository.LectureNotificationRepository;
import rs.devana.labs.studentinfo.infrastructure.repository.LectureRepository;
import rs.devana.labs.studentinfo.infrastructure.repository.NotificationRepository;

@Module
public class RepositoryModule {

    @Provides
    public NotificationRepositoryInterface ProvideNotificationRepository(ApiDataFetch apiDataFetch, NotificationParser notificationParser){
        return new NotificationRepository(apiDataFetch, notificationParser);
    }

    @Provides
    public LectureRepositoryInterface ProvideLectureRepository(ApiDataFetch apiDataFetch, LectureParser lectureParser){
        return new LectureRepository(apiDataFetch, lectureParser);
    }
}
