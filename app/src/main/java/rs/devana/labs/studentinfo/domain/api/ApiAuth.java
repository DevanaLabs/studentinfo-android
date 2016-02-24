package rs.devana.labs.studentinfo.domain.api;

import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;

import javax.inject.Inject;

import rs.devana.labs.studentinfo.domain.http.HttpClientInterface;

public class ApiAuth {
    SharedPreferences sharedPreferences;
    HttpClientInterface httpClient;
    private static final String TAG = ApiAuth.class.getSimpleName();
    static ResponseReader responseReader = new ResponseReader();
    static String url = "http://api.studentinfo.rs";

    @Inject
    public ApiAuth(SharedPreferences sharedPreferences, HttpClientInterface httpClient) {
        this.sharedPreferences = sharedPreferences;
        this.httpClient = httpClient;
    }

    public boolean getAccessToken(String username, String password) {
        final String payload = "{\"grant_type\": \"password\",\"client_id\": \"1\", \"client_secret\": \"secret\", \"username\": \"" + username + "\", \"password\": \"" + password + "\"}";
        try {
            BufferedReader reader = httpClient.post(url + "/oauth/access_token", payload);
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
            BufferedReader reader = httpClient.post(url + "/auth", payload);
            String response = responseReader.readResponse(reader);
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
            BufferedReader reader = httpClient.delete(url + "/auth", payload);
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
            BufferedReader reader = httpClient.get(url + "/verifyAccessToken?access_token=" + accessToken);
            String response = responseReader.readResponse(reader);
            if (response.contains("success")) {
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
