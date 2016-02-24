package rs.devana.labs.studentinfo.infrastructure.dagger.module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import rs.devana.labs.studentinfo.domain.http.HttpClientInterface;
import rs.devana.labs.studentinfo.infrastructure.http.HttpClient;

@Module
public class HttpClientModule {

    @Provides
    @Singleton
    public HttpClientInterface provideHttpClient(){
        return new HttpClient();
    }
}
