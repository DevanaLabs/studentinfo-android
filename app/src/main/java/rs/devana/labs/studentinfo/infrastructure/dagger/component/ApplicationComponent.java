package rs.devana.labs.studentinfo.infrastructure.dagger.component;

import javax.inject.Singleton;

import dagger.Component;
import rs.devana.labs.studentinfo.infrastructure.dagger.module.ApiModule;
import rs.devana.labs.studentinfo.infrastructure.dagger.module.ApplicationContextModule;
import rs.devana.labs.studentinfo.infrastructure.dagger.module.EventBusModule;
import rs.devana.labs.studentinfo.infrastructure.dagger.module.HttpClientModule;
import rs.devana.labs.studentinfo.infrastructure.dagger.module.RepositoryModule;
import rs.devana.labs.studentinfo.infrastructure.services.gcm.GcmListeningService;
import rs.devana.labs.studentinfo.infrastructure.services.gcm.RegistrationIntentService;
import rs.devana.labs.studentinfo.presentation.fragments.FeedbackFragment;
import rs.devana.labs.studentinfo.presentation.fragments.NotificationsFragment;
import rs.devana.labs.studentinfo.presentation.fragments.SettingsFragment;
import rs.devana.labs.studentinfo.presentation.fragments.WeeklyScheduleFragment;
import rs.devana.labs.studentinfo.presentation.fragments.YearlyCalendarFragment;
import rs.devana.labs.studentinfo.presentation.login.LoginActivity;
import rs.devana.labs.studentinfo.presentation.main.NavigationDrawerActivity;

@Component(modules = {
        ApplicationContextModule.class,
        RepositoryModule.class,
        HttpClientModule.class,
        EventBusModule.class,
        ApiModule.class,
})

@Singleton
public interface ApplicationComponent {

    void inject(RegistrationIntentService registrationIntentService);

    void inject(LoginActivity loginActivity);

    void inject(NavigationDrawerActivity navigationDrawerActivity);

    void inject(GcmListeningService gcmListeningService);

    void inject(SettingsFragment settingsFragment);

    void inject(WeeklyScheduleFragment weeklyScheduleFragment);

    void inject(NotificationsFragment notificationsFragment);

    void inject(YearlyCalendarFragment yearlyCalendarFragment);

    void inject(FeedbackFragment feedbackFragment);
}
