package rs.devana.labs.studentinfoapp.infrastructure.json.parser;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.inject.Inject;

import rs.devana.labs.studentinfoapp.domain.models.notification.event.EventNotification;

public class EventNotificationParser {
    EventParser eventParser;

    @Inject
    public EventNotificationParser(EventParser eventParser) {
        this.eventParser = eventParser;
    }

    public EventNotification parse(JSONObject jsonNotification) {
        try {
            String stringNotification = jsonNotification.getString("expiresAt").substring(0, 19) + ".000-"+ jsonNotification.getString("expiresAt").substring(20, 24);

            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(df.parse(stringNotification));

            return new EventNotification(jsonNotification.getInt("id"), jsonNotification.getString("description"), calendar, eventParser.parse(jsonNotification.getJSONObject("event")));
        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }

        return null;
    }
}
