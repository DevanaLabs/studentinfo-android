package rs.devana.labs.studentinfo.infrastructure.dagger.module;

import android.content.SharedPreferences;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import rs.devana.labs.studentinfo.domain.api.ApiAuth;
import rs.devana.labs.studentinfo.domain.api.ApiPushNotifications;
import rs.devana.labs.studentinfo.domain.http.HttpClientInterface;
import rs.devana.labs.studentinfo.infrastructure.http.HttpClient;

@Module
public class ApiModule {

    @Provides
    @Singleton
    public ApiAuth provideApiAuth(HttpClientInterface httpClient, SharedPreferences sharedPreferences) {
        return new ApiAuth(sharedPreferences, httpClient);
    }

    @Provides
    @Singleton
    public ApiPushNotifications provideApiPushNotifications(SharedPreferences sharedPreferences, HttpClient httpClient) {
        return new ApiPushNotifications(sharedPreferences, httpClient);
    }
}
