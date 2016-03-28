package rs.devana.labs.studentinfoapp.infrastructure.http;


import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.inject.Inject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import rs.devana.labs.studentinfoapp.domain.http.HttpClientInterface;

public class HttpClient implements HttpClientInterface{

    private static final String TAG = OkHttpClient.class.getSimpleName();
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    OkHttpClient httpClient = new OkHttpClient();

    @Inject
    public HttpClient(){
    }

    public BufferedReader postStream(String url, String json) throws IOException {
        Log.i(TAG, "Sending a postStream request with body:\n" + json + "\n to URL: " + url);

        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = httpClient.newCall(request).execute();

        InputStream in = response.body().byteStream();

        return new BufferedReader(new InputStreamReader(in));
    }

    public BufferedReader putStream(String url, String json) throws IOException {
        Log.i(TAG, "Sending a putStream request with body:\n" + json + "\n to URL: " + url);

        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .put(body)
                .build();

        Response response = httpClient.newCall(request).execute();
        InputStream in = response.body().byteStream();

        return new BufferedReader(new InputStreamReader(in));
    }

    public BufferedReader getStream(String url) throws IOException {
        Log.i(TAG, "Sending a getStream request to URL: " + url);

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        Response response = httpClient.newCall(request).execute();
        InputStream in = response.body().byteStream();

        return new BufferedReader(new InputStreamReader(in));
    }

    public BufferedReader deleteStream(String url, String json) throws IOException {
        Log.i(TAG, "Sending a deleteStream request with body:\n" + json + "\n to URL: " + url);

        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .delete(body)
                .build();

        Response response = httpClient.newCall(request).execute();
        InputStream in = response.body().byteStream();

        return new BufferedReader(new InputStreamReader(in));
    }

    @Override
    public String post(String url, String json) throws IOException {
        Log.i(TAG, "Sending a post request with body:\n" + json + "\n to URL: " + url);

        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = httpClient.newCall(request).execute();
        return response.body().string();
    }

    @Override
    public String put(String url, String json) throws IOException {
        Log.i(TAG, "Sending a put request with body:\n" + json + "\n to URL: " + url);

        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .put(body)
                .build();

        Response response = httpClient.newCall(request).execute();
        return response.body().string();
    }

    @Override
    public String get(String url) throws IOException {
        Log.i(TAG, "Sending a get request to URL: " + url);

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        Response response = httpClient.newCall(request).execute();
        return response.body().string();
    }



    @Override
    public String delete(String url, String json) throws IOException {
        Log.i(TAG, "Sending a deleteStream request with body:\n" + json + "\n to URL: " + url);

        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .delete(body)
                .build();

        Response response = httpClient.newCall(request).execute();
        return response.body().string();
    }
}
