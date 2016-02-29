package rs.devana.labs.studentinfo.infrastructure.dagger;

import rs.devana.labs.studentinfo.infrastructure.core.StudentInfoApplication;
import rs.devana.labs.studentinfo.infrastructure.dagger.component.ApplicationComponent;
import rs.devana.labs.studentinfo.infrastructure.dagger.component.DaggerApplicationComponent;
import rs.devana.labs.studentinfo.infrastructure.dagger.module.ApiModule;
import rs.devana.labs.studentinfo.infrastructure.dagger.module.ApplicationContextModule;
import rs.devana.labs.studentinfo.infrastructure.dagger.module.EventBusModule;
import rs.devana.labs.studentinfo.infrastructure.dagger.module.HttpClientModule;
import rs.devana.labs.studentinfo.infrastructure.dagger.module.RepositoryModule;

public enum Injector {
    INSTANCE;

    ApplicationComponent applicationComponent;

    public void initializeApplicationComponent(StudentInfoApplication application) {
        this.applicationComponent = DaggerApplicationComponent.builder()
                .apiModule(new ApiModule())
                .repositoryModule(new RepositoryModule())
                .eventBusModule(new EventBusModule())
                .applicationContextModule(new ApplicationContextModule(application))
                .httpClientModule(new HttpClientModule())
                .build();
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }
}