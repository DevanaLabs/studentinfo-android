package rs.devana.labs.studentinfo.infrastructure.core;

import android.app.Application;

import rs.devana.labs.studentinfo.infrastructure.dagger.Injector;

public class StudentInfoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Injector.INSTANCE.initializeApplicationComponent(this);
    }
}
