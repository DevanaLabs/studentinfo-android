package rs.devana.labs.studentinfoapp.infrastructure.dagger.module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import rs.devana.labs.studentinfoapp.domain.http.HttpClientInterface;
import rs.devana.labs.studentinfoapp.infrastructure.http.HttpClient;

@Module
public class HttpClientModule {

    @Provides
    @Singleton
    public HttpClientInterface provideHttpClient(){
        return new HttpClient();
    }
}
