package rs.devana.labs.studentinfoapp.domain.models.event;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import rs.devana.labs.studentinfoapp.domain.models.classroom.Classroom;
import rs.devana.labs.studentinfoapp.domain.models.notification.event.EventNotification;


public class Event {

    private int id;
    private String type, description;
    private Calendar startsAt, endsAt;
    private List<Classroom> classrooms;
    private List<EventNotification> eventNotifications;

    @Inject
    public Event() {

    }

    public Event(int id, String type, String description, Calendar startsAt, Calendar endsAt, List<Classroom> classrooms, List<EventNotification> eventNotifications) {
        this.id = id;
        this.type = type;
        this.description = description;
        this.startsAt = startsAt;
        this.endsAt = endsAt;
        this.classrooms = classrooms;
        this.eventNotifications = eventNotifications;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Calendar getStartsAt() {
        return startsAt;
    }

    public void setStartsAt(Calendar startsAt) {
        this.startsAt = startsAt;
    }

    public Calendar getEndsAt() {
        return endsAt;
    }

    public void setEndsAt(Calendar endsAt) {
        this.endsAt = endsAt;
    }

    public List<Classroom> getClassrooms() {
        return classrooms;
    }

    public void setClassrooms(List<Classroom> classrooms) {
        this.classrooms = classrooms;
    }

    public List<EventNotification> getNotifications() {
        return eventNotifications;
    }

    public String getDayOfTheMonth(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd", Locale.getDefault());
        return simpleDateFormat.format(this.getStartsAt().getTime());
    }

    public int getMonth(){
        return this.getStartsAt().getTime().getMonth();
    }

    public void setNotifications(List<EventNotification> eventNotifications) {
        this.eventNotifications = eventNotifications;
    }

    @Override
    public String toString() {
        return "id=" + id +
                ", type='" + type + '\'' +
                ", description='" + description + '\'' +
                ", startsAt=" + startsAt +
                ", endsAt=" + endsAt +
                ", classrooms=" + classrooms +
                ", notifications=" + eventNotifications;
    }
}
