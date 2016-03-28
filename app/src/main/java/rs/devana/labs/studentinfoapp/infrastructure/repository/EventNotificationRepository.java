package rs.devana.labs.studentinfoapp.infrastructure.repository;

import java.util.List;

import javax.inject.Inject;

import rs.devana.labs.studentinfoapp.domain.models.notification.event.EventNotification;
import rs.devana.labs.studentinfoapp.domain.models.notification.event.EventNotificationRepositoryInterface;
import rs.devana.labs.studentinfoapp.infrastructure.json.parser.EventParser;

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
