package rs.devana.labs.studentinfo.infrastructure.dagger.module;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import rs.devana.labs.studentinfo.infrastructure.core.StudentInfoApplication;

@Module
public class ApplicationContextModule {
    private final StudentInfoApplication application;

    public ApplicationContextModule(StudentInfoApplication application) {
        this.application = application;
    }

    @Provides
    @Singleton
    public StudentInfoApplication provideApplication() {
        return this.application;
    }

    @Provides
    @Singleton
    public Context provideApplicationContext() {
        return this.application;
    }

    @Provides
    @Singleton
    SharedPreferences provideSharedPrefs() {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }

}
