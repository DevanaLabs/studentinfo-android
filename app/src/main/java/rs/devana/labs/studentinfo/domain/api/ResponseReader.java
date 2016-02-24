package rs.devana.labs.studentinfo.domain.api;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;

public class ResponseReader {
    private static final String TAG = ResponseReader.class.getSimpleName();

    public String readResponse(BufferedReader reader) {
        StringBuilder response = new StringBuilder();
        String line;
        try {
            Log.i(TAG, "Reading response.");
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            Log.i(TAG, "Received response:" + response.toString());

        } catch (IOException e) {
            Log.i(TAG, "There was an error reading data. Error message: " + e.getMessage());
            e.printStackTrace();
        }

        return response.toString();
    }
}