package rs.devana.labs.studentinfo.domain.http;

import java.io.BufferedReader;
import java.io.IOException;

public interface HttpClientInterface {

    BufferedReader post(String url, String json) throws IOException;

    BufferedReader get(String url) throws IOException;

    BufferedReader put(String url, String json) throws IOException;

    BufferedReader delete(String url, String json) throws IOException;
}
