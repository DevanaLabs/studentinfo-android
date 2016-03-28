package rs.devana.labs.studentinfoapp.domain.http;

import java.io.BufferedReader;
import java.io.IOException;

public interface HttpClientInterface {

    BufferedReader postStream(String url, String json) throws IOException;

    BufferedReader getStream(String url) throws IOException;

    BufferedReader putStream(String url, String json) throws IOException;

    BufferedReader deleteStream(String url, String json) throws IOException;

    String post(String url, String json) throws IOException;

    String get(String url) throws IOException;

    String put(String url, String json) throws IOException;

    String delete(String url, String json) throws IOException;
}
