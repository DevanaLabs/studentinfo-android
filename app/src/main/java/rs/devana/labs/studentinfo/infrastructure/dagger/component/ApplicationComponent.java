package rs.devana.labs.studentinfo.infrastructure.dagger.component;

import javax.inject.Singleton;

import dagger.Component;
import rs.devana.labs.studentinfo.infrastructure.dagger.module.ApplicationContextModule;
import rs.devana.labs.studentinfo.infrastructure.dagger.module.DummyModule;
import rs.devana.labs.studentinfo.presentation.main.MainActivity;

@Component(modules = {ApplicationContextModule.class, DummyModule.class})
@Singleton
public interface ApplicationComponent {
    void inject(MainActivity mainActivity);
}
