package rs.devana.labs.studentinfo.infrastructure.dagger.module;

import dagger.Module;
import dagger.Provides;
import rs.devana.labs.studentinfo.domain.api.ApiDataFetch;
import rs.devana.labs.studentinfo.domain.models.notification.NotificationRepositoryInterface;
import rs.devana.labs.studentinfo.infrastructure.repository.NotificationRepository;

@Module
public class NotificationRepositoryModule {

    @Provides
    public NotificationRepositoryInterface ProvideNotificationRepository(ApiDataFetch apiDataFetch){
        return new NotificationRepository(apiDataFetch);
    }
}
