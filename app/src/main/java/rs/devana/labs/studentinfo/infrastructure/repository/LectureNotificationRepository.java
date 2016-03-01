package rs.devana.labs.studentinfo.infrastructure.repository;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import javax.inject.Inject;

import rs.devana.labs.studentinfo.domain.models.notification.lecture.LectureNotification;
import rs.devana.labs.studentinfo.domain.models.notification.lecture.LectureNotificationRepositoryInterface;

public class LectureNotificationRepository implements LectureNotificationRepositoryInterface {
    @Inject
    public LectureNotificationRepository(){
    }

    @Override
    public List<LectureNotification> getLectureNotifications() {
        return null;
    }
}
