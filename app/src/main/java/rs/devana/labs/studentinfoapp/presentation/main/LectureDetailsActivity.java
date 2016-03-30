package rs.devana.labs.studentinfoapp.presentation.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import rs.devana.labs.studentinfoapp.R;
import rs.devana.labs.studentinfoapp.domain.models.lecture.Lecture;
import rs.devana.labs.studentinfoapp.domain.models.notification.lecture.LectureNotification;
import rs.devana.labs.studentinfoapp.infrastructure.dagger.Injector;
import rs.devana.labs.studentinfoapp.infrastructure.json.parser.LectureParser;
import rs.devana.labs.studentinfoapp.presentation.adapters.NotificationArrayAdapter;

public class LectureDetailsActivity extends AppCompatActivity {

    @Inject
    LectureParser lectureParser;

    private static final int SECONDS_IN_DAY = 86400;
    private static final int SECONDS_IN_HOUR = 3600;
    private static final int SECONDS_IN_MINUTE = 60;
    private static final int MINUTES_IN_HOURS = 60;
    private static final int HOURS_IN_DAY = 24;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Injector.INSTANCE.getApplicationComponent().inject(this);

        Intent intent = getIntent();

        Lecture lecture = lectureParser.getLecture(intent.getIntExtra("lectureId", 0));
        setContentView(R.layout.activity_lecture_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (lecture.getType() != 0) {
            toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.orange));
        }
        toolbar.setTitle(lecture.getCourse().getName());
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_arrow_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        TextView teacherName = (TextView) findViewById(R.id.teacherName);
        TextView lectureType = (TextView) findViewById(R.id.lectureType);
        TextView lectureTime = (TextView) findViewById(R.id.lectureTime);
        TextView classroomName = (TextView) findViewById(R.id.classroomName);

        teacherName.setTextColor(ContextCompat.getColor(this, R.color.teacherName));
        teacherName.setText(lecture.getTeacher());
        lectureType.setText(lecture.getTypeString());
        if (lecture.getType() != 0) {
            lectureTime.setTextColor(ContextCompat.getColor(this, R.color.orange));
        } else {
            lectureTime.setTextColor(ContextCompat.getColor(this, R.color.blue));
        }
        lectureTime.setText(getTime(lecture.getStartsAt(), lecture.getEndsAt()));
        classroomName.setText(lecture.getClassroom().getName());

        ListView notificationsListView = (ListView) findViewById(R.id.notificationsListView);
        if (lecture.getLectureNotifications().size() > 0) {
            NotificationArrayAdapter notificationArrayAdapter = new NotificationArrayAdapter(removeExpired(lecture.getLectureNotifications()), this, R.layout.custom_notification_card_view);
            notificationsListView.setAdapter(notificationArrayAdapter);
        }
    }

    private String getTime(int startsAt, int endsAt) {
        String time;
        switch (startsAt / SECONDS_IN_DAY) {
            case 0:
                time = getResources().getString(R.string.mondayLong);
                break;
            case 1:
                time = getResources().getString(R.string.thursdayLong);
                break;
            case 2:
                time = getResources().getString(R.string.thursdayLong);
                break;
            case 3:
                time = getResources().getString(R.string.thursdayLong);
                break;
            case 4:
                time = getResources().getString(R.string.thursdayLong);
                break;
            case 6:
                time = getResources().getString(R.string.thursdayLong);
                break;
            default:
                time = getResources().getString(R.string.thursdayLong);
                break;
        }
        time += ", ";
        time += (startsAt / SECONDS_IN_HOUR) % HOURS_IN_DAY + ":" + new DecimalFormat("00").format((startsAt / SECONDS_IN_MINUTE) % MINUTES_IN_HOURS);
        time += "-";
        time += (endsAt / SECONDS_IN_HOUR) % HOURS_IN_DAY + ":" + new DecimalFormat("00").format((endsAt / SECONDS_IN_MINUTE) % MINUTES_IN_HOURS);
        time += " (" + Math.round((float) (endsAt - startsAt) / SECONDS_IN_HOUR) + "h)";
        return time;
    }

    private List<LectureNotification> removeExpired(List<LectureNotification> lectureNotifications){
        for (int i = 0; i < lectureNotifications.size(); i++){
            if (lectureNotifications.get(i).getExpiresAt().getTimeInMillis() < Calendar.getInstance().getTimeInMillis()){
                lectureNotifications.remove(i);
            }
        }
        return lectureNotifications;
    }
}
