package rs.devana.labs.studentinfo.domain.models.event.GroupEvent;

import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import rs.devana.labs.studentinfo.domain.models.classroom.Classroom;
import rs.devana.labs.studentinfo.domain.models.event.Event;
import rs.devana.labs.studentinfo.domain.models.group.Group;
import rs.devana.labs.studentinfo.domain.models.notification.Notification;

public class GroupEvent extends Event {
    protected Group group;

    @Inject
    public GroupEvent() {

    }

    public GroupEvent(int id, String type, String description, Group group, Calendar startsAt, Calendar endsAt, List<Classroom> classrooms, List<Notification> notifications) {
        super(id, type, description, startsAt, endsAt, classrooms, notifications);
        this.group = group;
    }
}
