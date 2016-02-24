package rs.devana.labs.studentinfo.infrastructure.dagger;

import rs.devana.labs.studentinfo.infrastructure.core.StudentInfoApplication;
import rs.devana.labs.studentinfo.infrastructure.dagger.component.ApplicationComponent;
import rs.devana.labs.studentinfo.infrastructure.dagger.component.DaggerApplicationComponent;
import rs.devana.labs.studentinfo.infrastructure.dagger.module.ApiPushNotificationsModule;
import rs.devana.labs.studentinfo.infrastructure.dagger.module.ApplicationContextModule;
import rs.devana.labs.studentinfo.infrastructure.dagger.module.DummyModule;
import rs.devana.labs.studentinfo.infrastructure.dagger.module.HttpClientModule;

public enum Injector {
    INSTANCE;

    ApplicationComponent applicationComponent;

    public void initializeApplicationComponent(StudentInfoApplication application) {
        this.applicationComponent = DaggerApplicationComponent.builder()
                .apiPushNotificationsModule(new ApiPushNotificationsModule())
                .applicationContextModule(new ApplicationContextModule(application))
                .dummyModule(new DummyModule())
                .httpClientModule(new HttpClientModule())
                .build();
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }
}