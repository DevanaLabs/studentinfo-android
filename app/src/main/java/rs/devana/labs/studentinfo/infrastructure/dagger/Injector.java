package rs.devana.labs.studentinfo.infrastructure.dagger;

import rs.devana.labs.studentinfo.infrastructure.core.StudentInfoApplication;
import rs.devana.labs.studentinfo.infrastructure.dagger.component.ApplicationComponent;
import rs.devana.labs.studentinfo.infrastructure.dagger.component.DaggerApplicationComponent;
import rs.devana.labs.studentinfo.infrastructure.dagger.module.ApplicationContextModule;
import rs.devana.labs.studentinfo.infrastructure.dagger.module.DummyModule;

public enum Injector {
    INSTANCE;

    ApplicationComponent applicationComponent;

    public void initializeApplicationComponent(StudentInfoApplication application) {
        this.applicationComponent = DaggerApplicationComponent.builder()
                .applicationContextModule(new ApplicationContextModule(application))
                .dummyModule(new DummyModule())
                .build();
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }
}