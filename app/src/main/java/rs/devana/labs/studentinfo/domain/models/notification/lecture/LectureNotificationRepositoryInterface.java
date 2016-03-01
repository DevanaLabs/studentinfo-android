package rs.devana.labs.studentinfo.domain.models.notification.lecture;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public interface LectureNotificationRepositoryInterface {
    List<LectureNotification> getLectureNotifications();
}
