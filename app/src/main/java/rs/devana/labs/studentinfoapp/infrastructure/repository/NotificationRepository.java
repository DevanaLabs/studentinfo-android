package rs.devana.labs.studentinfoapp.infrastructure.repository;

import org.json.JSONArray;

import javax.inject.Inject;

import rs.devana.labs.studentinfoapp.domain.api.ApiDataFetch;
import rs.devana.labs.studentinfoapp.domain.models.notification.NotificationRepositoryInterface;
import rs.devana.labs.studentinfoapp.infrastructure.json.parser.NotificationParser;

public class NotificationRepository implements NotificationRepositoryInterface {
    ApiDataFetch apiDataFetch;
    NotificationParser notificationParser;

    @Inject
    public NotificationRepository(ApiDataFetch apiDataFetch, NotificationParser notificationParser) {
        this.apiDataFetch = apiDataFetch;
        this.notificationParser = notificationParser;
    }

    @Override
    public JSONArray getAllNotifications() {
        return apiDataFetch.getAllNotifications();
    }
}
