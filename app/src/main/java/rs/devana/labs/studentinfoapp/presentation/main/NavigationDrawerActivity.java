package rs.devana.labs.studentinfoapp.presentation.main;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;

import javax.inject.Inject;

import rs.devana.labs.studentinfoapp.R;
import rs.devana.labs.studentinfoapp.domain.api.ApiAuth;
import rs.devana.labs.studentinfoapp.domain.api.ApiDataFetch;
import rs.devana.labs.studentinfoapp.domain.models.event.Event;
import rs.devana.labs.studentinfoapp.domain.models.lecture.Lecture;
import rs.devana.labs.studentinfoapp.domain.models.notification.NotificationRepositoryInterface;
import rs.devana.labs.studentinfoapp.infrastructure.dagger.Injector;
import rs.devana.labs.studentinfoapp.infrastructure.event_bus_events.ChooseGroupEvent;
import rs.devana.labs.studentinfoapp.infrastructure.event_bus_events.GroupChangedEvent;
import rs.devana.labs.studentinfoapp.infrastructure.event_bus_events.LogoutFinishedEvent;
import rs.devana.labs.studentinfoapp.infrastructure.event_bus_events.OpenEventActivityEvent;
import rs.devana.labs.studentinfoapp.infrastructure.event_bus_events.OpenLectureFragmentEvent;
import rs.devana.labs.studentinfoapp.infrastructure.event_bus_events.ScheduleFetchedEvent;
import rs.devana.labs.studentinfoapp.presentation.fragments.AboutFragment;
import rs.devana.labs.studentinfoapp.presentation.fragments.FeedbackFragment;
import rs.devana.labs.studentinfoapp.presentation.fragments.NotificationsFragment;
import rs.devana.labs.studentinfoapp.presentation.fragments.SettingsFragment;
import rs.devana.labs.studentinfoapp.presentation.fragments.WeeklyScheduleFragment;
import rs.devana.labs.studentinfoapp.presentation.fragments.YearlyCalendarFragment;
import rs.devana.labs.studentinfoapp.presentation.login.LoginActivity;

public class NavigationDrawerActivity extends AppCompatActivity {

    @Inject
    public SharedPreferences sharedPreferences;

    @Inject
    public ApiAuth apiAuth;

    @Inject
    public ApiDataFetch apiDataFetch;

    @Inject
    NotificationRepositoryInterface notificationRepository;

    @Inject
    EventBus eventBus;

    private static final String TAG = NavigationDrawerActivity.class.getSimpleName();
    private static final int WEEKLY_SCHEDULE_INDEX = 0;
    private static final int SETTINGS_INDEX = 3;
    private static final int NOTIFICATION_INDEX = 2;

    String email;
    TextView groupTextView;
    ProgressDialog loggingOutDialog;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Injector.INSTANCE.getApplicationComponent().inject(this);

        PreferenceManager.setDefaultValues(this, R.xml.settings_fragment, false);

        setContentView(R.layout.activity_navigation_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);

        Intent intent = this.getIntent();
        if (intent!=null && intent.getStringExtra("fragment")!= null){
            if (intent.getStringExtra("fragment").equals("notifications")) {
                handleNotifications();
            }
        } else {
            handleWeeklySchedule();
        }

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();

                switch (id){
                    case R.id.nav_weekly_schedule: handleWeeklySchedule();
                        break;
                    case R.id.nav_yearly_calendar: handleYearlyCalendar();
                        break;
                    case R.id.nav_notifications: handleNotifications();
                        break;
                    case R.id.nav_settings: handleSettings();
                        break;
                    case R.id.nav_about: handleAbout();
                        break;
                    case R.id.nav_feedback: handleFeedback();
                        break;
                    case R.id.nav_logout: handleLogout();
                        break;

                }

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        navigationView.getMenu().getItem(0).setChecked(true);

        View v = navigationView.getHeaderView(0);
        TextView emailTextView = (TextView) v.findViewById(R.id.emailTextView);
        email = sharedPreferences.getString("email", "");
        emailTextView.setText(email);
        groupTextView = (TextView) v.findViewById(R.id.groupTextView);
        String group = sharedPreferences.getString("groupName", "");
        groupTextView.setText(String.format(getResources().getString(R.string.group), group.equals("") ? getResources().getString(R.string.notChosen) : group));

        if (Integer.valueOf(sharedPreferences.getString("groups", "0")) == 0){
            navigationView.setCheckedItem(R.id.nav_settings);
            handleSettings();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            finish();
        } else {
            drawer.openDrawer(GravityCompat.START);
        }
    }

    private boolean logout(String accessToken, String deviceToken) {
        apiAuth.deactivateDeviceToken(deviceToken, accessToken);
        return apiAuth.logout(accessToken);
    }

    private void setPreferences() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("accessToken", "");
        editor.putString("slug", "");
        editor.putInt("userId", 0);
        editor.apply();
    }

    private void handleWeeklySchedule() {
        Log.i(TAG, "Entering weekly schedule view.");

        Fragment fragment = WeeklyScheduleFragment.newInstance();
        changeToFragment(fragment);
        navigationView.getMenu().getItem(WEEKLY_SCHEDULE_INDEX).setChecked(true);

        try {
            getSupportActionBar().setTitle(R.string.weeklySchedule);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void handleYearlyCalendar() {
        Log.i(TAG, "Entering yearly calendar view.");

        Fragment fragment = YearlyCalendarFragment.newInstance();
        changeToFragment(fragment);

        try {
            getSupportActionBar().setTitle(R.string.yearlyCalender);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void handleNotifications() {
        Log.i(TAG, "Entering notifications view.");

        Fragment fragment = NotificationsFragment.newInstance();
        changeToFragment(fragment);
        navigationView.getMenu().getItem(NOTIFICATION_INDEX).setChecked(true);

        try {
            getSupportActionBar().setTitle(R.string.notifications);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void handleSettings() {
        Log.i(TAG, "Entering settings view.");

        Fragment fragment = SettingsFragment.newInstance();
        changeToFragment(fragment);
        navigationView.getMenu().getItem(SETTINGS_INDEX).setChecked(true);

        try {
            getSupportActionBar().setTitle(R.string.settings);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void handleAbout(){
        Log.i(TAG, "Entering about view.");

        Fragment fragment = AboutFragment.newInstance();
        changeToFragment(fragment);

        try {
            getSupportActionBar().setTitle(R.string.aboutUs);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void handleLecture(Lecture lecture) {
        Log.i(TAG, "Entering lecture view.");

        Intent intent = new Intent(this, LectureDetailsActivity.class).putExtra("lectureId", lecture.getId());
        startActivity(intent);
    }

    private void handleEvent(Event event) {
        Log.i(TAG, "Entering event view.");

        Intent intent = new Intent(this, EventDetailsActivity.class).putExtra("eventId", event.getId());
        startActivity(intent);
    }

    private void changeToFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_navigation_drawer, fragment)
                .commit();
    }

    private void handleLogout() {
        final Context context = this;
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_menu_logout)
                .setTitle(getString(R.string.logout))
                .setMessage(getString(R.string.logoutConfirmation))
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new UserLogoutTask().execute();
                        loggingOutDialog = ProgressDialog.show(context, getResources().getString(R.string.pleaseWait), getResources().getString(R.string.logout), true);
                        loggingOutDialog.show();

                        Log.i(TAG, "Logging out.");
                    }

                })
                .setNegativeButton(getString(R.string.no), null)
                .show();

    }

    private void handleFeedback() {
        changeToFragment(FeedbackFragment.newInstance());
        try {
            getSupportActionBar().setTitle(R.string.feedback);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Subscribe
    public void onGroupChangedEvent(GroupChangedEvent groupChangedEvent){
        groupTextView.setText(String.format(getResources().getString(R.string.group), groupChangedEvent.group));
    }

    @Subscribe
    public void onScheduleFetchedEvent(ScheduleFetchedEvent scheduleFetchedEvent){
        handleWeeklySchedule();
    }

    @Subscribe
    public void onLogoutFinishedEvent(LogoutFinishedEvent logoutFinishedEvent){
        loggingOutDialog.dismiss();
    }


    @Subscribe
    public void onOpenLectureFragmentEvent(OpenLectureFragmentEvent openLectureFragmentEvent) {
        handleLecture(openLectureFragmentEvent.getLecture());
    }

    @Subscribe
    public void onOpenEventActivityEvent(OpenEventActivityEvent openEventActivityEvent) {
        handleEvent(openEventActivityEvent.getEvent());
    }

    @Override
    protected void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

    @Subscribe
    public void onChooseGroupEvent(ChooseGroupEvent chooseGroupEvent){
        handleSettings();
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    public class UserLogoutTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            String accessToken = sharedPreferences.getString("accessToken", "");
            String deviceToken = sharedPreferences.getString("deviceToken", "");
            boolean success = logout(accessToken, deviceToken);
            setPreferences();

            return success;
        }

        @Override
        protected void onPostExecute(final Boolean success) {

            if (success) {
                Intent intent = new Intent(NavigationDrawerActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();

                eventBus.post(new LogoutFinishedEvent());
            }
        }
    }

}