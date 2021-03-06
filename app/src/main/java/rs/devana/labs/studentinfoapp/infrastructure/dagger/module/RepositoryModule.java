package rs.devana.labs.studentinfoapp.infrastructure.dagger.module;

import dagger.Module;
import dagger.Provides;
import rs.devana.labs.studentinfoapp.domain.api.ApiDataFetch;
import rs.devana.labs.studentinfoapp.domain.models.event.EventRepositoryInterface;
import rs.devana.labs.studentinfoapp.domain.models.lecture.LectureRepositoryInterface;
import rs.devana.labs.studentinfoapp.domain.models.notification.NotificationRepositoryInterface;
import rs.devana.labs.studentinfoapp.infrastructure.json.parser.EventParser;
import rs.devana.labs.studentinfoapp.infrastructure.json.parser.LectureParser;
import rs.devana.labs.studentinfoapp.infrastructure.json.parser.NotificationParser;
import rs.devana.labs.studentinfoapp.infrastructure.repository.EventRepository;
import rs.devana.labs.studentinfoapp.infrastructure.repository.LectureRepository;
import rs.devana.labs.studentinfoapp.infrastructure.repository.NotificationRepository;

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

    @Provides
    public EventRepositoryInterface ProvideEventRepositoryInterface(ApiDataFetch apiDataFetch, EventParser eventParser) {
        return new EventRepository(apiDataFetch, eventParser);
    }
}
