package rs.devana.labs.studentinfoapp.domain.models.notification.lecture;

import java.util.Calendar;

import rs.devana.labs.studentinfoapp.domain.models.lecture.Lecture;
import rs.devana.labs.studentinfoapp.domain.models.notification.Notification;

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

    @Override
    public String getAdditionalInfo() {
        return lecture.getCourse().getName();
    }
}
