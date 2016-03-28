package rs.devana.labs.studentinfoapp.domain.models.event.GlobalEvent;

import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;


import rs.devana.labs.studentinfoapp.domain.models.classroom.Classroom;
import rs.devana.labs.studentinfoapp.domain.models.event.Event;
import rs.devana.labs.studentinfoapp.domain.models.notification.event.EventNotification;

public class GlobalEvent extends Event {

    @Inject
    public GlobalEvent() {

    }

    public GlobalEvent(int id, String type, String description, Calendar startsAt, Calendar endsAt, List<Classroom> classrooms, List<EventNotification> eventNotifications) {
        super(id, type, description, startsAt, endsAt, classrooms, eventNotifications);
    }
}

