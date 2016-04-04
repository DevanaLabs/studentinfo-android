package rs.devana.labs.studentinfoapp.presentation.login;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;

import javax.inject.Inject;

import rs.devana.labs.studentinfoapp.R;
import rs.devana.labs.studentinfoapp.domain.api.ApiAuth;
import rs.devana.labs.studentinfoapp.domain.api.ApiDataFetch;
import rs.devana.labs.studentinfoapp.infrastructure.dagger.Injector;
import rs.devana.labs.studentinfoapp.infrastructure.event_bus_events.EventsFetchedEvent;
import rs.devana.labs.studentinfoapp.infrastructure.event_bus_events.GroupsFetchedEvent;
import rs.devana.labs.studentinfoapp.infrastructure.event_bus_events.LoginErrorEvent;
import rs.devana.labs.studentinfoapp.infrastructure.event_bus_events.NotificationsFetchedEvent;
import rs.devana.labs.studentinfoapp.infrastructure.services.gcm.RegistrationIntentService;
import rs.devana.labs.studentinfoapp.presentation.fragments.NotificationsFragment;
import rs.devana.labs.studentinfoapp.presentation.main.NavigationDrawerActivity;

public class LoginActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    @Inject
    SharedPreferences sharedPreferences;
    @Inject
    ApiAuth apiAuth;
    @Inject
    ApiDataFetch apiDataFetch;
    @Inject
    EventBus eventBus;

    private UserLoginTask mAuthTask = null;

    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private boolean auth = false, userNotInDB = false;
    String email, password;
    String accessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Injector.INSTANCE.getApplicationComponent().inject(this);

        eventBus.register(this);
        accessToken = sharedPreferences.getString("accessToken", "");
        final CountDownLatch latch = new CountDownLatch(1);

        if (!isNetworkAvailable()) {
            if (accessToken.equals("")) {
                internetUnavailable();
            } else {
                Intent navigationDrawerIntent = new Intent(LoginActivity.this, NavigationDrawerActivity.class);
                startActivity(navigationDrawerIntent);
            }
        } else {
            if (!accessToken.equals("")) {
                Thread verifyTokenThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            auth = apiAuth.verifyAccessToken(accessToken);
                            latch.countDown();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                verifyTokenThread.start();

                try {
                    latch.await();
                    if (auth) {
                        Intent navigationDrawer = new Intent(this, NavigationDrawerActivity.class);
                        startActivity(navigationDrawer);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
            mEmailView.setText(sharedPreferences.getString("textEmail", ""));
            mEmailView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_NEXT){
                        mPasswordView.requestFocus();
                        return true;
                    }
                    return false;
                }
            });

            mPasswordView = (EditText) findViewById(R.id.password);
            mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                    if (id == R.id.login || id == EditorInfo.IME_ACTION_DONE) {
                        attemptLogin();
                        return true;
                    }
                    return false;
                }
            });

            Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
            mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    attemptLogin();
                }
            });

            TextView forgotPasswordTextView = (TextView) findViewById(R.id.forgotPasswordTextView);
            forgotPasswordTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent forgotPasswordIntent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                    startActivity(forgotPasswordIntent);
                }
            });

            TextView youDontHaveAnAccountTextView = (TextView) findViewById(R.id.youDontHaveAnAccountTextView);
            youDontHaveAnAccountTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    makeDialog();
                }
            });

            mLoginFormView = findViewById(R.id.login_form);
            mProgressView = findViewById(R.id.login_progress);
        }
    }

    private void attemptLogin() {

        if (!isNetworkAvailable()) {
            internetUnavailable();
            return;
        }
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        mEmailView.setError(null);
        mPasswordView.setError(null);
        email = mEmailView.getText().toString();
        password = mPasswordView.getText().toString();

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("email", email);
        editor.apply();

        boolean cancel = false;
        View focusView = null;
        if (!isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();

    }

    private boolean isPasswordValid(String password) {
        return ((password != null) && (!password.isEmpty()));
    }

    @Override
    protected void onDestroy() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("textEmail", mEmailView.getText().toString());
        editor.apply();

        eventBus.unregister(this);
        super.onDestroy();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            userNotInDB = false;
            return apiAuth.getUser(mEmail, mPassword) && apiAuth.getAccessToken(mEmail, mPassword);
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                accessToken = sharedPreferences.getString("accessToken", "");

                new GroupFetchTask().execute(); new EventsFetchTask().execute(); new NotificationsFetchTask().execute();

                Intent gcmRegister = new Intent(LoginActivity.this, RegistrationIntentService.class);
                startService(gcmRegister);
                finish();

                Intent navigationDrawerIntent = new Intent(LoginActivity.this, NavigationDrawerActivity.class);
                startActivity(navigationDrawerIntent);
            } else if (!userNotInDB){
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return ((activeNetworkInfo != null) && activeNetworkInfo.isConnected());
    }

    private void internetUnavailable() {
        Toast toast = Toast.makeText(this, R.string.networkConnection, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP, 0, 150);

        toast.show();
    }

    private void makeDialog(){
        new AlertDialog.Builder(this)
                .setTitle(R.string.note)
                .setMessage(R.string.loginRemark)
                .setNegativeButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }

    @Subscribe
    public void onLoginErrorEvent(LoginErrorEvent loginErrorEvent){
        final String errorMsg = loginErrorEvent.getErrorMessage();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mEmailView.setError(errorMsg);
                mEmailView.requestFocus();
                userNotInDB = true;
            }
        });
    }

    @Subscribe
    public void onEventsFetchedEvent(EventsFetchedEvent eventsFetchedEvent){
    }

    public class GroupFetchTask extends AsyncTask<Void, Void, JSONArray> {

        @Override
        protected JSONArray doInBackground(Void... params) {

            return apiDataFetch.getAllGroups();
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("allGroups", jsonArray.toString());
            editor.apply();

            eventBus.post(new GroupsFetchedEvent(jsonArray.toString()));
        }
    }

    public class EventsFetchTask extends AsyncTask<Void, Void, JSONArray> {

        @Override
        protected JSONArray doInBackground(Void... params) {

            return apiDataFetch.getAllEvents();
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("allEvents", jsonArray.toString());
            editor.apply();

            eventBus.post(new EventsFetchedEvent(jsonArray.toString()));
        }
    }

    public class NotificationsFetchTask extends AsyncTask<Void, Void, JSONArray> {

        @Override
        protected JSONArray doInBackground(Void... params) {

            return apiDataFetch.getAllNotifications();
        }

        @Override
        protected void onPostExecute(JSONArray jsonNotifications) {

            for (int i = 0; i < jsonNotifications.length(); i++) {
                try {
                    if (!jsonNotifications.getJSONObject(i).has("arrived")) {
                        String arrived = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault()).format(Calendar.getInstance().getTime());
                        jsonNotifications.getJSONObject(i).put("arrived", arrived);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("notifications", jsonNotifications.toString());
            editor.apply();

            eventBus.post(new NotificationsFetchedEvent(jsonNotifications.toString()));
        }
    }

}
