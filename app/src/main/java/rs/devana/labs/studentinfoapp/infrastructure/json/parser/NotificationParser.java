package rs.devana.labs.studentinfoapp.infrastructure.json.parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import rs.devana.labs.studentinfoapp.domain.models.notification.Notification;

public class NotificationParser {

    @Inject
    public NotificationParser() {
    }

    public List<Notification> parse(JSONArray jsonNotifications){

        List<Notification> notifications = new ArrayList<>();

        int i = 0;
        while (i < jsonNotifications.length()) {
            try {
                JSONObject jsonNotification = jsonNotifications.getJSONObject(i);
                String stringNotification = jsonNotification.getString("expiresAt").substring(0, 19) + ".000-"+ jsonNotification.getString("expiresAt").substring(20, 24);

                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(dateFormat.parse(stringNotification));

                Calendar calendarArrived = Calendar.getInstance();
                if (jsonNotification.has("arrived")) {
                    calendarArrived.setTime(dateFormat.parse(jsonNotification.getString("arrived")));
                } else {
                    calendarArrived.setTime(dateFormat.parse(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault()).format(Calendar.getInstance().getTime())));
                }

                if (jsonNotification.has("event"))
                    notifications.add(new Notification(jsonNotification.getInt("id"), jsonNotification.getString("description"), calendar, calendarArrived, jsonNotification.getJSONObject("event").getString("description")));
                if (jsonNotification.has("lecture"))
                    notifications.add(new Notification(jsonNotification.getInt("id"), jsonNotification.getString("description"), calendar, calendarArrived, jsonNotification.getJSONObject("lecture").getJSONObject("course").getString("name")));
            } catch (JSONException | ParseException e) {
                e.printStackTrace();
            } finally {
                i++;
            }
        }
        return notifications;
    }

    public Notification parse(JSONObject jsonNotification) {
        try {
            String stringNotification = jsonNotification.getString("expiresAt").substring(0, 19) + ".000-" + jsonNotification.getString("expiresAt").substring(20, 24);

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateFormat.parse(stringNotification));

            Calendar calendarArrived = Calendar.getInstance();
            if (jsonNotification.has("arrived")) {
                calendarArrived.setTime(dateFormat.parse(jsonNotification.getString("arrived")));
            } else {
                calendarArrived.setTime(dateFormat.parse(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault()).format(Calendar.getInstance().getTime())));
            }

            if (jsonNotification.has("event"))
                return new Notification(jsonNotification.getInt("id"), jsonNotification.getString("description"), calendar, calendarArrived, jsonNotification.getJSONObject("event").getString("description"));
            if (jsonNotification.has("lecture"))
                return new Notification(jsonNotification.getInt("id"), jsonNotification.getString("description"), calendar, calendarArrived, jsonNotification.getJSONObject("lecture").getJSONObject("course").getString("name"));
        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
