package rs.devana.labs.studentinfo.domain.models.event;

import java.util.Calendar;
import java.util.List;

import rs.devana.labs.studentinfo.domain.models.classroom.Classroom;
import rs.devana.labs.studentinfo.domain.models.notification.Notification;

public class Event {

    private int id;
    private String type, description;
    private Calendar startsAt, endsAt;
    private List<Classroom> classrooms;
    private List<Notification> notifications;

    public Event(int id, String type, String description, Calendar startsAt, Calendar endsAt, List<Classroom> classrooms, List<Notification> notifications){
        this.id = id;
        this.type = type;
        this.description = description;
        this.startsAt = startsAt;
        this.endsAt = endsAt;
        this.classrooms = classrooms;
        this.notifications = notifications;
    }

    @Override
    public String toString() {
        return "id=" + id +
                ", type='" + type + '\'' +
                ", description='" + description + '\'' +
                ", startsAt=" + startsAt +
                ", endsAt=" + endsAt +
                ", classrooms=" + classrooms +
                ", notifications=" + notifications;
    }
}
