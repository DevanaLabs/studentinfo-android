package rs.devana.labs.studentinfoapp.domain.models.event.GlobalEvent;

import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import rs.devana.labs.studentinfoapp.domain.models.classroom.Classroom;
import rs.devana.labs.studentinfoapp.domain.models.event.Event;
import rs.devana.labs.studentinfoapp.domain.models.notification.Notification;

public class GlobalEvent extends Event {

    @Inject
    public GlobalEvent() {

    }

    public GlobalEvent(int id, String type, String description, Calendar startsAt, Calendar endsAt, List<Classroom> classrooms, List<Notification> notifications) {
        super(id, type, description, startsAt, endsAt, classrooms, notifications);
    }
}

