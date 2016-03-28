package rs.devana.labs.studentinfoapp.domain.models.notification.event;

import java.util.Calendar;

import rs.devana.labs.studentinfoapp.domain.models.event.Event;
import rs.devana.labs.studentinfoapp.domain.models.notification.Notification;

public class EventNotification extends Notification{

    private Event event;

    public EventNotification(int id, String description, Calendar expiresAt, Calendar arrived, String additionInfo, Event event) {
        super(id, description, expiresAt, arrived, additionInfo);
        this.event = event;
    }

    @Override
    public String toString() {
        return "event=" + event;
    }
}
