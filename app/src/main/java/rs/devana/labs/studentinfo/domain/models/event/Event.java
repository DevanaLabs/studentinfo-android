package rs.devana.labs.studentinfo.domain.models.event;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import rs.devana.labs.studentinfo.domain.models.classroom.Classroom;
import rs.devana.labs.studentinfo.domain.models.notification.Notification;

public class Event {

    private int id;
    private String type, description;
    private Calendar startsAt, endsAt;
    private List<Classroom> classrooms;
    private List<Notification> notifications;

    public Event(){}

    public Event(int id, String type, String description, Calendar startsAt, Calendar endsAt, List<Classroom> classrooms, List<Notification> notifications){
        this.id = id;
        this.type = type;
        this.description = description;
        this.startsAt = startsAt;
        this.endsAt = endsAt;
        this.classrooms = classrooms;
        this.notifications = notifications;
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

    public List<Notification> getNotifications() {
        return notifications;
    }

    public String getDayOfTheMonth(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd", Locale.getDefault());
        return simpleDateFormat.format(this.getStartsAt().getTime());
    }

    public int getMonth(){
        return this.getStartsAt().getTime().getMonth();
    }

    public void setNotifications(List<Notification> notifications) {
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
