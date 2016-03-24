package rs.devana.labs.studentinfo.domain.api;

import android.content.SharedPreferences;

import java.io.BufferedReader;

import javax.inject.Inject;

import rs.devana.labs.studentinfo.domain.http.HttpClientInterface;

public class ApiAccount {

    SharedPreferences sharedPreferences;
    HttpClientInterface httpClient;
    ResponseReader responseReader;

    static String url = "http://api.studentinfo.rs";

    @Inject
    public ApiAccount(SharedPreferences sharedPreferences, HttpClientInterface httpClient, ResponseReader responseReader) {
        this.sharedPreferences = sharedPreferences;
        this.httpClient = httpClient;
        this.responseReader = responseReader;
    }

    public boolean sendRecoveryEmail(String email) {
        try {
            BufferedReader reader = httpClient.getStream(url + "/register/recoverPassword/" + email);
            String response = responseReader.readResponse(reader);
            return response.contains("success");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean changePassword(String password, String newPassword){
        try {
            int userId = sharedPreferences.getInt("userId", 0);
            String accessToken = sharedPreferences.getString("accessToken", "");

            final String payload = "{\"currentPassword\": \"" + password + "\", \"password\": \"" + newPassword + "\", \"password_confirmation\": \"" + newPassword + "\", \"access_token\": \"" + accessToken + "\"}";
            BufferedReader reader = httpClient.postStream(url + "/user/" + userId, payload);
            String response = responseReader.readResponse(reader);
            return response.contains("success");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
