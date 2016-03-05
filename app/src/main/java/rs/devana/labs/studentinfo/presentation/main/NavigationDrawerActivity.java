package rs.devana.labs.studentinfo.presentation.main;

import android.content.Intent;
import android.content.SharedPreferences;
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

import javax.inject.Inject;

import rs.devana.labs.studentinfo.R;
import rs.devana.labs.studentinfo.domain.api.ApiAuth;
import rs.devana.labs.studentinfo.domain.api.ApiDataFetch;
import rs.devana.labs.studentinfo.infrastructure.dagger.Injector;
import rs.devana.labs.studentinfo.infrastructure.event_bus_events.GroupChangedEvent;
import rs.devana.labs.studentinfo.presentation.fragments.NotificationsFragment;
import rs.devana.labs.studentinfo.presentation.fragments.SettingsFragment;
import rs.devana.labs.studentinfo.presentation.fragments.WeeklyScheduleFragment;
import rs.devana.labs.studentinfo.presentation.fragments.YearlyCalendarFragment;
import rs.devana.labs.studentinfo.presentation.login.LoginActivity;

public class NavigationDrawerActivity extends AppCompatActivity {

    @Inject
    public SharedPreferences sharedPreferences;

    @Inject
    public ApiAuth apiAuth;

    @Inject
    public ApiDataFetch apiDataFetch;

    private Toolbar toolbar;
    private static final String TAG = NavigationDrawerActivity.class.getSimpleName();
    String email;
    TextView groupTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        Injector.INSTANCE.getApplicationComponent().inject(this);

        PreferenceManager.setDefaultValues(this, R.xml.settings_fragment, false);

        setContentView(R.layout.activity_navigation_drawer);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        handleWeeklySchedule();

        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.nav_weekly_schedule) {
                    handleWeeklySchedule();
                } else if (id == R.id.nav_yearly_calendar) {
                    handleYearlyCalendar();
                } else if (id == R.id.nav_logout) {
                    handleLogout();
                } else if (id == R.id.nav_notifications) {
                    handleNotifications();
                } else if (id == R.id.nav_settings) {
                    handleSettings();
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

        //TODO: Treba pronaci grupu po id-u jer sharedPreferences [groups] vraca id a ne ime grupe pa ce se prikazivati Grupa 15 umesto Grupa 107 npr.
        groupTextView.setText(String.format(getResources().getString(R.string.group), sharedPreferences.getString("groups", "")));

        if (Integer.valueOf(sharedPreferences.getString("groups", "0")) == 0){
            navigationView.setCheckedItem(R.id.nav_settings);
            handleSettings();
        }

        getNotifications();
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
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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

        toolbar.setTitle(R.string.weeklySchedule);
    }

    private void handleYearlyCalendar() {
        Log.i(TAG, "Entering yearly calendar view.");

        Fragment fragment = YearlyCalendarFragment.newInstance();
        changeToFragment(fragment);

        toolbar.setTitle(R.string.yearlyCalender);
    }

    private void handleNotifications() {
        Log.i(TAG, "Entering notifications view.");

        Fragment fragment = NotificationsFragment.newInstance();
        changeToFragment(fragment);

        toolbar.setTitle(R.string.notifications);
    }

    private void handleSettings() {
        Log.i(TAG, "Entering settings view.");

        Fragment fragment = SettingsFragment.newInstance();
        changeToFragment(fragment);

        toolbar.setTitle(R.string.settings);
    }

    private void changeToFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_navigation_drawer, fragment)
                .commit();
    }

    private void handleLogout() {
        final Thread logout = new Thread(new Runnable() {
            @Override
            public void run() {
                String accessToken = sharedPreferences.getString("accessToken", "");
                String deviceToken = sharedPreferences.getString("deviceToken", "");
                logout(accessToken, deviceToken);
                setPreferences();
                Intent intent = new Intent(NavigationDrawerActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        logout.start();
        Log.i(TAG, "Logging out.");
    }

    private void getNotifications(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("notifications", apiDataFetch.getAllNotifications().toString());
        editor.apply();
    }

    @Subscribe
    public void onGroupChangedEvent(GroupChangedEvent groupChangedEvent){
        groupTextView.setText(String.format(getResources().getString(R.string.group), groupChangedEvent.group));
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
}