package rs.devana.labs.studentinfo.infrastructure.http;


import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.inject.Inject;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.OkHttpClient;
import rs.devana.labs.studentinfo.domain.http.HttpClientInterface;

public class HttpClient implements HttpClientInterface{

    private static final String TAG = OkHttpClient.class.getSimpleName();

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    OkHttpClient httpClient = new OkHttpClient();

    @Inject
    public HttpClient(){
    }

    public BufferedReader post(String url, String json) throws IOException {
        Log.i(TAG, "Sending a post request with body:\n" + json + "\n to URL: " + url);

        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = httpClient.newCall(request).execute();

        InputStream in = response.body().byteStream();

        return new BufferedReader(new InputStreamReader(in));
    }

    public BufferedReader put(String url, String json) throws IOException {
        Log.i(TAG, "Sending a put request with body:\n" + json + "\n to URL: " + url);

        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .put(body)
                .build();

        Response response = httpClient.newCall(request).execute();
        InputStream in = response.body().byteStream();

        return new BufferedReader(new InputStreamReader(in));
    }

    public BufferedReader get(String url) throws IOException {
        Log.i(TAG, "Sending a get request to URL: " + url);

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        Response response = httpClient.newCall(request).execute();
        InputStream in = response.body().byteStream();

        return new BufferedReader(new InputStreamReader(in));
    }

    public BufferedReader delete(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Log.i(TAG, "Sending a delete request with body:\n" + json + "\n to URL: " + url);

        Request request = new Request.Builder()
                .url(url)
                .delete(body)
                .build();

        Response response = httpClient.newCall(request).execute();
        InputStream in = response.body().byteStream();

        return new BufferedReader(new InputStreamReader(in));
    }
}
