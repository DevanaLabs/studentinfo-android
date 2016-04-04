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

import rs.devana.labs.studentinfoapp.domain.models.notification.event.EventNotification;

public class EventNotificationParser {

    @Inject
    public EventNotificationParser() {
    }

    public EventNotification parse(JSONObject jsonEventNotification) {
        try {
            String stringNotification = jsonEventNotification.getString("expiresAt").substring(0, 19) + ".000+" + jsonEventNotification.getString("expiresAt").substring(20, 24);

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateFormat.parse(stringNotification));

            Calendar calendarArrived = Calendar.getInstance();
            calendarArrived.setTime(dateFormat.parse(jsonEventNotification.getString("arrived")));

            return new EventNotification(jsonEventNotification.getInt("id"), jsonEventNotification.getString("description"), calendar, calendarArrived, jsonEventNotification.getJSONObject("event").getString("description"), null);
        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<EventNotification> parse(JSONArray jsonNotifications) {
        List<EventNotification> eventNotifications = new ArrayList<>();

        for (int i = 0; i < jsonNotifications.length(); i++) {
            try {
                String stringNotification = jsonNotifications.getJSONObject(i).getString("expiresAt").substring(0, 19) + ".000+" + jsonNotifications.getJSONObject(i).getString("expiresAt").substring(20, 24);

                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(dateFormat.parse(stringNotification));

                Calendar calendarArrived = Calendar.getInstance();
                if (jsonNotifications.getJSONObject(i).has("arrived")) {
                    calendarArrived.setTime(dateFormat.parse(jsonNotifications.getJSONObject(i).getString("arrived")));
                } else {
                    calendarArrived.setTime(dateFormat.parse(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault()).format(Calendar.getInstance().getTime())));
                }

                eventNotifications.add(new EventNotification(jsonNotifications.getJSONObject(i).getInt("id"), jsonNotifications.getJSONObject(i).getString("description"), calendar, calendarArrived, jsonNotifications.getJSONObject(i).has("event") ? jsonNotifications.getJSONObject(i).getJSONObject("event").getString("description") : "", null));
            } catch (JSONException | ParseException e) {
                e.printStackTrace();
            }
        }

        return eventNotifications;
    }
}
