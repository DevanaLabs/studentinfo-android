package rs.devana.labs.studentinfoapp.domain.models.notification.event;

import java.util.Calendar;

import rs.devana.labs.studentinfoapp.domain.models.event.Event;
import rs.devana.labs.studentinfoapp.domain.models.notification.Notification;

public class EventNotification extends Notification{

    private Event event;

    public EventNotification(int id, String description, Calendar expiresAt, Event event) {
        super(id, description, expiresAt);
        this.event = event;
    }

    @Override
    public String toString() {
        return "event=" + event;
    }

    @Override
    public String getAdditionalInfo() {
        return event.getType();
    }
}
