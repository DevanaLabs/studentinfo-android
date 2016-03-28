package rs.devana.labs.studentinfoapp.domain.models.notification.event;

import java.util.List;

public interface EventNotificationRepositoryInterface {

    List<EventNotification> getEventNotifications();
}
