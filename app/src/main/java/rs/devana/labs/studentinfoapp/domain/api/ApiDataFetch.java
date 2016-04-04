package rs.devana.labs.studentinfoapp.domain.api;

import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;

import javax.inject.Inject;

import rs.devana.labs.studentinfoapp.domain.http.HttpClientInterface;

public class ApiDataFetch {
    SharedPreferences sharedPreferences;
    HttpClientInterface httpClient;
    ResponseReader responseReader;
    static String url = "http://api.studentinfo.rs";
    private static final String TAG = ApiDataFetch.class.getSimpleName();

    @Inject
    public ApiDataFetch(SharedPreferences sharedPreferences, HttpClientInterface httpClient, ResponseReader responseReader) {
        this.sharedPreferences = sharedPreferences;
        this.httpClient = httpClient;
        this.responseReader = responseReader;
    }

    public JSONArray getLecturesForGroup(int groupId) {
        String accessToken = sharedPreferences.getString("accessToken", "");
        String slug = sharedPreferences.getString("slug", "");
        try {
            BufferedReader reader = httpClient.getStream(url + "/" + slug + "/group/" + groupId + "?&access_token=" + accessToken);
            String response = responseReader.readResponse(reader);
            JSONObject json = new JSONObject(response);

            JSONObject success = json.getJSONObject("success");
            JSONObject data = success.getJSONObject("data");
            JSONObject group = data.getJSONObject("group");

            return group.getJSONArray("lectures");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JSONArray();
    }

    public JSONArray getAllGroups() {
        String accessToken = sharedPreferences.getString("accessToken", "");
        String slug = sharedPreferences.getString("slug", "");
        try {
            BufferedReader reader = httpClient.getStream(url + "/" + slug + "/groups?display=limited&access_token=" + accessToken);
            String response = responseReader.readResponse(reader);

            if (response.contains("success")) {
                JSONObject json = new JSONObject(response);
                JSONObject success = json.getJSONObject("success");

                return success.getJSONArray("data");

            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public JSONArray getAllNotifications() {
        String accessToken = sharedPreferences.getString("accessToken", "");
        String slug = sharedPreferences.getString("slug", "");
        try {
            BufferedReader reader = httpClient.getStream(url + "/" + slug + "/notifications/" + "?access_token=" + accessToken);
            String response = responseReader.readResponse(reader);
            JSONObject json = new JSONObject(response);

            JSONObject success = json.getJSONObject("success");

            return success.getJSONArray("data");
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return new JSONArray();
    }

    public JSONArray getAllEvents() {
        String accessToken = sharedPreferences.getString("accessToken", "");
        String slug = sharedPreferences.getString("slug", "");
        try {
            BufferedReader reader = httpClient.getStream(url + "/" + slug + "/events/" + "?display=notification&access_token=" + accessToken);
            String response = responseReader.readResponse(reader);
            JSONObject json = new JSONObject(response);

            JSONObject success = json.getJSONObject("success");

            return success.getJSONArray("data");
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return new JSONArray();
    }
}
