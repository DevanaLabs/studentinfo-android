package rs.devana.labs.studentinfo.infrastructure.dagger.component;

import javax.inject.Singleton;

import dagger.Component;
import rs.devana.labs.studentinfo.domain.api.ApiAuth;
import rs.devana.labs.studentinfo.domain.api.ApiPushNotifications;
import rs.devana.labs.studentinfo.domain.services.gcm.RegistrationIntentService;
import rs.devana.labs.studentinfo.infrastructure.dagger.module.ApiAuthModule;
import rs.devana.labs.studentinfo.infrastructure.dagger.module.ApiPushNotificationsModule;
import rs.devana.labs.studentinfo.infrastructure.dagger.module.ApplicationContextModule;
import rs.devana.labs.studentinfo.infrastructure.dagger.module.DummyModule;
import rs.devana.labs.studentinfo.infrastructure.dagger.module.EventBusModule;
import rs.devana.labs.studentinfo.infrastructure.dagger.module.HttpClientModule;
import rs.devana.labs.studentinfo.infrastructure.dagger.module.NotificationRepositoryModule;
import rs.devana.labs.studentinfo.presentation.main.LoginActivity;
import rs.devana.labs.studentinfo.presentation.main.MainActivity;

@Component(modules = {
        ApplicationContextModule.class,
        NotificationRepositoryModule.class,
        ApiPushNotificationsModule.class,
        HttpClientModule.class,
        EventBusModule.class,
        ApiAuthModule.class,
        DummyModule.class,
})
@Singleton
public interface ApplicationComponent {
    void inject(MainActivity mainActivity);
    void inject(RegistrationIntentService registrationIntentService);
    void inject(LoginActivity loginActivity);
}
