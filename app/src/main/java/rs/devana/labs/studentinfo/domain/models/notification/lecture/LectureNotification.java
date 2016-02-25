package rs.devana.labs.studentinfo.domain.models.notification.lecture;

import java.util.Calendar;

import rs.devana.labs.studentinfo.domain.models.lecture.Lecture;
import rs.devana.labs.studentinfo.domain.models.notification.Notification;

public class LectureNotification extends Notification{

    private Lecture lecture;

    public LectureNotification(int id, String description, Calendar expiresAt, Lecture lecture) {
        super(id, description, expiresAt);
        this.lecture = lecture;
    }

    @Override
    public String toString() {
        return "lecture=" + lecture;
    }
}
