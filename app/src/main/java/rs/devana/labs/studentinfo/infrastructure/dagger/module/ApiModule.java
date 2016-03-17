package rs.devana.labs.studentinfo.infrastructure.dagger.module;

import android.content.SharedPreferences;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import rs.devana.labs.studentinfo.domain.api.ApiAuth;
import rs.devana.labs.studentinfo.domain.api.ApiDataFetch;
import rs.devana.labs.studentinfo.domain.api.ApiFeedback;
import rs.devana.labs.studentinfo.domain.api.ApiPushNotifications;
import rs.devana.labs.studentinfo.domain.api.ResponseReader;
import rs.devana.labs.studentinfo.domain.http.HttpClientInterface;
import rs.devana.labs.studentinfo.infrastructure.http.HttpClient;

@Module
public class ApiModule {

    @Provides
    @Singleton
    public ApiAuth provideApiAuth(SharedPreferences sharedPreferences, HttpClientInterface httpClient, ResponseReader responseReader) {
        return new ApiAuth(sharedPreferences, httpClient, responseReader);
    }

    @Provides
    @Singleton
    public ApiPushNotifications provideApiPushNotifications(SharedPreferences sharedPreferences, HttpClient httpClient, ResponseReader responseReader) {
        return new ApiPushNotifications(sharedPreferences, httpClient, responseReader);
    }

    @Provides
    @Singleton
    public ApiDataFetch provideApiDataFetch(SharedPreferences sharedPreferences, HttpClientInterface httpClientInterface, ResponseReader responseReader) {
        return new ApiDataFetch(sharedPreferences, httpClientInterface, responseReader);
    }

    @Provides
    @Singleton
    public ApiFeedback provideApiFeedback(SharedPreferences sharedPreferences, HttpClientInterface httpClientInterface, ResponseReader responseReader) {
        return new ApiFeedback(sharedPreferences, httpClientInterface, responseReader);
    }
}
