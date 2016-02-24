package rs.devana.labs.studentinfo.presentation.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import javax.inject.Inject;

import rs.devana.labs.studentinfo.R;
import rs.devana.labs.studentinfo.domain.dummy.Dummy;
import rs.devana.labs.studentinfo.domain.dummy.DummyRepository;
import rs.devana.labs.studentinfo.infrastructure.dagger.Injector;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

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
    }
}
