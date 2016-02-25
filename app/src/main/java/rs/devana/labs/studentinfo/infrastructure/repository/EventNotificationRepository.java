package rs.devana.labs.studentinfo.infrastructure.repository;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import rs.devana.labs.studentinfo.domain.models.notification.event.EventNotification;
import rs.devana.labs.studentinfo.domain.models.notification.event.EventNotificationRepositoryInterface;

public class EventNotificationRepository implements EventNotificationRepositoryInterface {
    EventRepository eventRepository;

    @Inject
    public EventNotificationRepository(EventRepository eventRepository){
        this.eventRepository = eventRepository;
    }

    @Override
    public List<EventNotification> getEventNotifications() {
        return null;
    }

    @Override
    public EventNotification getEventNotification(JSONObject jsonNotification) {
        try {
            String stringNotification = jsonNotification.getString("expiresAt").substring(0, 19) + ".000-"+ jsonNotification.getString("expiresAt").substring(20, 24);

            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(df.parse(stringNotification));

            return new EventNotification(jsonNotification.getInt("id"), jsonNotification.getString("description"), calendar, eventRepository.getEvent(jsonNotification.getJSONObject("event")));
        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }

        return null;
    }
}
