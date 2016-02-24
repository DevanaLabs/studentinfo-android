package rs.devana.labs.studentinfo.infrastructure.dagger.module;

import android.content.SharedPreferences;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import rs.devana.labs.studentinfo.domain.api.ApiAuth;
import rs.devana.labs.studentinfo.domain.http.HttpClientInterface;

@Module
public class ApiAuthModule {

    @Provides
    @Singleton
    public ApiAuth provideApiAuth(HttpClientInterface httpClient, SharedPreferences sharedPreferences) {
        return new ApiAuth(sharedPreferences, httpClient);
    }
}
