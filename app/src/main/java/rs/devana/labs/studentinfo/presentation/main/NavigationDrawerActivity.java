package rs.devana.labs.studentinfo.presentation.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import javax.inject.Inject;

import rs.devana.labs.studentinfo.R;
import rs.devana.labs.studentinfo.domain.api.ApiAuth;
import rs.devana.labs.studentinfo.infrastructure.dagger.Injector;

public class NavigationDrawerActivity extends AppCompatActivity {

    @Inject
    public SharedPreferences sharedPreferences;

    @Inject
    public ApiAuth apiAuth;

    private Toolbar toolbar;
    private static final String TAG = NavigationDrawerActivity.class.getSimpleName();
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Injector.INSTANCE.getApplicationComponent().inject(this);
        setContentView(R.layout.activity_navigation_drawer);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        Log.i("Email:", email = sharedPreferences.getString("email", ""));

        handleWeeklySchedule();

        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
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
        emailTextView.setText(email);
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
        //TODO: implement the method
        Log.i(TAG, "Entering weekly schedule view.");

        toolbar.setTitle(R.string.weeklySchedule);
    }

    private void handleYearlyCalendar() {
        //TODO: implement the method
        Log.i(TAG, "Entering yearly calendar view.");

        toolbar.setTitle(R.string.yearlyCalender);
    }

    private void handleNotifications() {
        //TODO: implement the method
        Log.i(TAG, "Entering notifications view.");

        toolbar.setTitle(R.string.notifications);
    }

    private void handleSettings() {
        Log.i(TAG, "Entering settings view.");

//        Fragment fragment = SettingsFragment.newInstance();

        // Insert the fragment by replacing any existing fragment
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        fragmentManager.beginTransaction()
//                .replace(R.id.content_navigation_drawer, fragment)
//                .commit();

        toolbar.setTitle(R.string.settings);
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
}