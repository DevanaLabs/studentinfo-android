package rs.devana.labs.studentinfo.domain.api;

import android.content.SharedPreferences;

import java.io.BufferedReader;

import javax.inject.Inject;

import rs.devana.labs.studentinfo.domain.http.HttpClientInterface;

public class ApiForgotPassword {

    SharedPreferences sharedPreferences;
    HttpClientInterface httpClient;
    ResponseReader responseReader;

    static String url = "http://api.studentinfo.rs";

    @Inject
    public ApiForgotPassword(SharedPreferences sharedPreferences, HttpClientInterface httpClient, ResponseReader responseReader) {
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
}
