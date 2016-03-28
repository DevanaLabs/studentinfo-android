package rs.devana.labs.studentinfoapp.infrastructure.core;

import android.app.Application;

import rs.devana.labs.studentinfoapp.infrastructure.dagger.Injector;

public class StudentInfoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Injector.INSTANCE.initializeApplicationComponent(this);
    }
}
