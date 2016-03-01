package rs.devana.labs.studentinfo.domain.models.notification.event;

import org.json.JSONObject;

import java.util.List;

public interface EventNotificationRepositoryInterface {

    List<EventNotification> getEventNotifications();
}
