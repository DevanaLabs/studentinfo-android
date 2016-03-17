package rs.devana.labs.studentinfo.domain.api;

import android.content.SharedPreferences;

import com.google.android.gms.common.api.Api;

import org.json.JSONObject;

import java.io.BufferedReader;

import javax.inject.Inject;

import rs.devana.labs.studentinfo.domain.http.HttpClientInterface;

public class ApiFeedback {
    SharedPreferences sharedPreferences;
    HttpClientInterface httpClient;
    ResponseReader responseReader;
    private static final String TAG = ApiAuth.class.getSimpleName();
    private static final String CLIENT_ID = "\"client_id\": \"1\"";
    static String url = "http://api.studentinfo.rs";

    @Inject
    public ApiFeedback(SharedPreferences sharedPreferences, HttpClientInterface clientInterface, ResponseReader responseReader) {
        this.sharedPreferences = sharedPreferences;
        this.httpClient = clientInterface;
        this.responseReader = responseReader;
    }

    public boolean sendFeedback(String content) {
        String accessToken = sharedPreferences.getString("accessToken", "");
        String slug = sharedPreferences.getString("slug", "raf");
        try {
            JSONObject payload = new JSONObject();
            payload.put("access_token", accessToken);
            payload.put("text", "Android: " + content);
            BufferedReader reader = httpClient.postStream(url + "/" + slug + "/feedback", payload.toString());
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
