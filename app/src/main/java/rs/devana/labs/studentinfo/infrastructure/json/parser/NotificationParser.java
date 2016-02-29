package rs.devana.labs.studentinfo.infrastructure.json.parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import rs.devana.labs.studentinfo.domain.models.notification.Notification;

public class NotificationParser {

    public List<Notification> parse(JSONArray jsonNotifications){

        List<Notification> notifications = new ArrayList<>();

        int i = 0;
        while (i < jsonNotifications.length()) {
            try {
                JSONObject jsonNotification = jsonNotifications.getJSONObject(i);
                String stringNotification = jsonNotification.getString("expiresAt").substring(0, 19) + ".000-"+ jsonNotification.getString("expiresAt").substring(20, 24);

                DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(df.parse(stringNotification));

                Notification notification = new Notification(jsonNotification.getInt("id"), jsonNotification.getString("description"), calendar);
                notifications.add(notification);
            } catch (JSONException | ParseException e) {
                e.printStackTrace();
            } finally {
                i++;
            }
        }
        return notifications;
    }
}
