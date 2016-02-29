package rs.devana.labs.studentinfo.infrastructure.repository;

import org.json.JSONArray;

import java.util.List;

import javax.inject.Inject;

import rs.devana.labs.studentinfo.domain.api.ApiDataFetch;
import rs.devana.labs.studentinfo.domain.models.notification.Notification;
import rs.devana.labs.studentinfo.domain.models.notification.NotificationRepositoryInterface;
import rs.devana.labs.studentinfo.infrastructure.json.parser.NotificationParser;

public class NotificationRepository implements NotificationRepositoryInterface {
    ApiDataFetch apiDataFetch;
    NotificationParser notificationParser;

    @Inject
    public NotificationRepository(ApiDataFetch apiDataFetch, NotificationParser notificationParser){
        this.apiDataFetch = apiDataFetch;
        this.notificationParser = notificationParser;
    }

    @Override
    public List<Notification> getAllNotifications() {
        JSONArray jsonNotifications = apiDataFetch.getAllNotifications();

        return notificationParser.parse(jsonNotifications);
    }
}
