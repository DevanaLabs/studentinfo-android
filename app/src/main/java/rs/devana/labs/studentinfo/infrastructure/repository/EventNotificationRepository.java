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
import rs.devana.labs.studentinfo.infrastructure.json.parser.EventParser;

public class EventNotificationRepository implements EventNotificationRepositoryInterface {
    EventParser eventParser;

    @Inject
    public EventNotificationRepository(EventParser eventParser){
        this.eventParser = eventParser;
    }

    @Override
    public List<EventNotification> getEventNotifications() {
        return null;
    }
}
