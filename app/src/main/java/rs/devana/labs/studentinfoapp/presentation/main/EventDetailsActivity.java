package rs.devana.labs.studentinfoapp.presentation.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import rs.devana.labs.studentinfoapp.R;
import rs.devana.labs.studentinfoapp.domain.models.event.Event;
import rs.devana.labs.studentinfoapp.domain.models.lecture.Lecture;
import rs.devana.labs.studentinfoapp.domain.models.notification.event.EventNotification;
import rs.devana.labs.studentinfoapp.infrastructure.dagger.Injector;
import rs.devana.labs.studentinfoapp.infrastructure.json.parser.EventParser;
import rs.devana.labs.studentinfoapp.presentation.adapters.NotificationArrayAdapter;

public class EventDetailsActivity extends AppCompatActivity {

    @Inject
    EventParser eventParser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Injector.INSTANCE.getApplicationComponent().inject(this);

        Intent intent = getIntent();

        Event event= eventParser.getEvent(intent.getIntExtra("eventId", 0));
        setContentView(R.layout.activity_event_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setTitle(event.getType());
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_arrow_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        TextView description = (TextView) findViewById(R.id.description);
        TextView startTime = (TextView) findViewById(R.id.startTime);
        TextView endTime = (TextView) findViewById(R.id.endTime);

        String arrived = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault()).format(Calendar.getInstance().getTime());
        String startTimeString = getResources().getString(R.string.startTime) + ": " + new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(event.getStartsAt().getTime());
        String endTimeString = getResources().getString(R.string.endTime) + ": " + new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(event.getEndsAt().getTime());

        description.setText(event.getDescription());
        startTime.setText(startTimeString);
        endTime.setText(endTimeString);

        ListView notificationsListView = (ListView) findViewById(R.id.notificationsListView);
        List<EventNotification> eventNotifications = removeExpired(event.getNotifications());
        if (eventNotifications.size() > 0) {
            NotificationArrayAdapter notificationArrayAdapter = new NotificationArrayAdapter(eventNotifications, this);
            notificationsListView.setAdapter(notificationArrayAdapter);
        }
    }

    private List<EventNotification> removeExpired(List<EventNotification> eventNotifications){
        for (int i = 0; i < eventNotifications.size(); i++){
            if (eventNotifications.get(i).getExpiresAt().getTimeInMillis() < Calendar.getInstance().getTimeInMillis()){
                eventNotifications.remove(i);
            }
        }
        return eventNotifications;
    }
}
