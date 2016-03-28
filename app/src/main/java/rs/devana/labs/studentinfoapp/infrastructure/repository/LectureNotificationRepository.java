package rs.devana.labs.studentinfoapp.infrastructure.repository;

import java.util.List;

import javax.inject.Inject;

import rs.devana.labs.studentinfoapp.domain.models.notification.lecture.LectureNotification;
import rs.devana.labs.studentinfoapp.domain.models.notification.lecture.LectureNotificationRepositoryInterface;

public class LectureNotificationRepository implements LectureNotificationRepositoryInterface {
    @Inject
    public LectureNotificationRepository(){
    }

    @Override
    public List<LectureNotification> getLectureNotifications() {
        return null;
    }
}
