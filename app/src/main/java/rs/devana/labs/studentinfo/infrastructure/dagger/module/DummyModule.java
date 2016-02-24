package rs.devana.labs.studentinfo.infrastructure.dagger.module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import rs.devana.labs.studentinfo.domain.dummy.DummyRepository;
import rs.devana.labs.studentinfo.infrastructure.repository.InMemoryDummyRepository;

@Module
public class DummyModule {

    @Provides
    @Singleton
    public DummyRepository provideDummyRepository() {
        return new InMemoryDummyRepository();
    }

}
