package rs.devana.labs.studentinfo.presentation.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import rs.devana.labs.studentinfo.R;
import rs.devana.labs.studentinfo.domain.dummy.Dummy;
import rs.devana.labs.studentinfo.domain.dummy.DummyRepository;
import rs.devana.labs.studentinfo.infrastructure.dagger.Injector;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Inject
    EventBus eventBus;

    @Inject
    DummyRepository dummyRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Injector.INSTANCE.getApplicationComponent().inject(this);

        for (Dummy dummy : dummyRepository.getAll()) {
            Log.i(TAG, dummy.getName());
        }

        eventBus.register(this);
        eventBus.post(new BigLoadEvent("Hello application!"));
    }

    @Override
    protected void onStop() {
        super.onStop();
        eventBus.unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onBigLoadEvent(BigLoadEvent event) {
        Log.i(TAG, "Big load event received");
        try {
            Thread.sleep(2000);
            eventBus.post(new LoadedEvent(event.getMessage(), 2));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void whenLoadCompletes(LoadedEvent event) {
        Log.i(TAG, "Loaded event received");
        Toast.makeText(this, event.getMessage(), Toast.LENGTH_SHORT).show();
        Toast.makeText(this, event.getDuration() + " seconds.", Toast.LENGTH_SHORT).show();
    }

    private class BigLoadEvent {
        private String message;

        public BigLoadEvent(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    private class LoadedEvent {
        private String message;
        private int duration;

        public LoadedEvent(String message, int duration) {
            this.message = message;
            this.duration = duration;
        }

        public String getMessage() {
            return message;
        }

        public int getDuration() {
            return duration;
        }
    }
}
