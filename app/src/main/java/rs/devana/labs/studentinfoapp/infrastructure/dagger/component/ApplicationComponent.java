package rs.devana.labs.studentinfoapp.infrastructure.dagger.component;

import javax.inject.Singleton;

import dagger.Component;
import rs.devana.labs.studentinfoapp.infrastructure.dagger.module.ApiModule;
import rs.devana.labs.studentinfoapp.infrastructure.dagger.module.ApplicationContextModule;
import rs.devana.labs.studentinfoapp.infrastructure.dagger.module.EventBusModule;
import rs.devana.labs.studentinfoapp.infrastructure.dagger.module.HttpClientModule;
import rs.devana.labs.studentinfoapp.infrastructure.dagger.module.RepositoryModule;
import rs.devana.labs.studentinfoapp.infrastructure.services.gcm.GcmListeningService;
import rs.devana.labs.studentinfoapp.infrastructure.services.gcm.RegistrationIntentService;
import rs.devana.labs.studentinfoapp.presentation.fragments.FeedbackFragment;
import rs.devana.labs.studentinfoapp.presentation.fragments.LectureFragment;
import rs.devana.labs.studentinfoapp.presentation.fragments.NotificationsFragment;
import rs.devana.labs.studentinfoapp.presentation.fragments.SettingsFragment;
import rs.devana.labs.studentinfoapp.presentation.fragments.WeeklyScheduleFragment;
import rs.devana.labs.studentinfoapp.presentation.fragments.YearlyCalendarFragment;
import rs.devana.labs.studentinfoapp.presentation.login.ForgotPasswordActivity;
import rs.devana.labs.studentinfoapp.presentation.login.LoginActivity;
import rs.devana.labs.studentinfoapp.presentation.main.LectureDetailsActivity;
import rs.devana.labs.studentinfoapp.presentation.main.NavigationDrawerActivity;
import rs.devana.labs.studentinfoapp.presentation.settings.ChangePasswordActivity;
import rs.devana.labs.studentinfoapp.presentation.views.LecturesView;

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

    void inject(ForgotPasswordActivity forgotPasswordActivity);

    void inject(FeedbackFragment feedbackFragment);

    void inject(LectureFragment lectureFragment);

    void inject(LecturesView lecturesView);

    void inject(LectureDetailsActivity lectureDetailsActivity);

    void inject(ChangePasswordActivity changePasswordActivity);
}
