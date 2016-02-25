package rs.devana.labs.studentinfo.domain.api;

import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;

import javax.inject.Inject;

import rs.devana.labs.studentinfo.domain.http.HttpClientInterface;

public class ApiPushNotifications {
    SharedPreferences sharedPreferences;
    HttpClientInterface httpClient;
    static ResponseReader responseReader = new ResponseReader();
    static String url = "http://api.studentinfo.rs";
    private static final String TAG = ApiPushNotifications.class.getSimpleName();

    @Inject
    public ApiPushNotifications(SharedPreferences sharedPreferences, HttpClientInterface httpClient){
        this.sharedPreferences = sharedPreferences;
        this.httpClient = httpClient;
    }

    public boolean postDeviceToken(String token) {
        String accessToken = sharedPreferences.getString("accessToken", "");
        if (!accessToken.equals("")) {
            final String payload = "{\"token\": \"" + token + "\", \"access_token\": \"" + accessToken + "\", \"active\": \"" + 1 + "\"}";
            try {
                BufferedReader reader = httpClient.post(url + "/deviceToken", payload);
                String response = responseReader.readResponse(reader);

                JSONObject json = new JSONObject(response);
                if (json.has("success")) {
                    return true;
                }
            } catch (JSONException e) {
                Log.i(TAG, "JSONException occurred:" + e.getMessage());
                e.printStackTrace();
            } catch (IOException e) {
                Log.i(TAG, "IOException occurred:" + e.getMessage());
                e.printStackTrace();
            }
        }
        return false;
    }
}
