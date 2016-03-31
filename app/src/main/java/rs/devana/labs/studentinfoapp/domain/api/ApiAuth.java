package rs.devana.labs.studentinfoapp.domain.api;

import android.content.SharedPreferences;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;

import javax.inject.Inject;

import rs.devana.labs.studentinfoapp.domain.http.HttpClientInterface;
import rs.devana.labs.studentinfoapp.infrastructure.dagger.Injector;
import rs.devana.labs.studentinfoapp.infrastructure.event_bus_events.LoginErrorEvent;

public class ApiAuth {
    SharedPreferences sharedPreferences;
    HttpClientInterface httpClient;
    ResponseReader responseReader;
    private static final String TAG = ApiAuth.class.getSimpleName();
    private static final int USER_NOT_IN_DB = 103;
    private static final String CLIENT_ID = "\"client_id\": \"1\"";
    static String url = "http://api.studentinfo.rs";
    @Inject
    EventBus eventBus;

    @Inject
    public ApiAuth(SharedPreferences sharedPreferences, HttpClientInterface httpClient, ResponseReader responseReader) {
        this.sharedPreferences = sharedPreferences;
        this.httpClient = httpClient;
        this.responseReader = responseReader;
        Injector.INSTANCE.getApplicationComponent().inject(this);
    }

    public boolean getAccessToken(String username, String password) {
        final String payload = "{\"grant_type\": \"password\","+CLIENT_ID+", \"client_secret\": \"secret\", \"username\": \"" + username + "\", \"password\": \"" + password + "\"}";
        try {
            BufferedReader reader = httpClient.postStream(url + "/oauth/access_token", payload);
            String response = responseReader.readResponse(reader);
            if (!response.contains("\"success\"")) {
                return false;
            }
            JSONObject json = new JSONObject(response);

            JSONObject success = json.getJSONObject("success");
            JSONObject data = success.getJSONObject("data");
            JSONObject oauth = data.getJSONObject("oauth");

            String accessToken = oauth.getString("access_token");

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("accessToken", accessToken);
            editor.apply();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean getUser(String email, String password) {
        final String payload = "{\"email\": \"" + email + "\", \"password\": \"" + password + "\"}";
        try {
            BufferedReader reader = httpClient.postStream(url + "/auth", payload);
            String response = responseReader.readResponse(reader);
            if (response.contains("error")){
                JSONObject json = new JSONObject(response);
                JSONObject error = json.getJSONObject("error");
                Log.i("GRESKA", error.toString());
                int errorCode = error.getInt("errorCode");
                if (errorCode == USER_NOT_IN_DB) {
                    eventBus.post(new LoginErrorEvent(errorCode));
                }
            }
            if (!response.contains("success")) {
                return false;
            }
            JSONObject json = new JSONObject(response);
            JSONObject success = json.getJSONObject("success");
            JSONObject data = success.getJSONObject("data");
            JSONObject user = data.getJSONObject("user");

            Log.i("USER: ", user.toString());

            int userId = user.getInt("id");
            String slug = user.getJSONObject("faculty").getString("slug");

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("slug", slug);
            editor.putInt("userId", userId);
            editor.apply();

            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean logout(String accessToken) {
        try {
            String payload = "{\"access_token\": \"" + accessToken + "\"}";
            BufferedReader reader = httpClient.deleteStream(url + "/auth", payload);
            String response = responseReader.readResponse(reader);
            JSONObject json = new JSONObject(response);
            if (json.has("success")) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean verifyAccessToken(String accessToken) {
        try {
            BufferedReader reader = httpClient.getStream(url + "/verifyAccessToken?access_token=" + accessToken);
            String response = responseReader.readResponse(reader);
            if (response.contains("success")) {
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean deactivateDeviceToken(String deviceToken, String accessToken) {
        final String payload = "{\"active\": " + 0 + ",\"access_token\": \"" + accessToken + "\"}";
        try {
            BufferedReader reader = httpClient.putStream(url + "/deviceToken/" + deviceToken, payload);
            String response = responseReader.readResponse(reader);
            JSONObject json = new JSONObject(response);
            if (json.has("success")) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
