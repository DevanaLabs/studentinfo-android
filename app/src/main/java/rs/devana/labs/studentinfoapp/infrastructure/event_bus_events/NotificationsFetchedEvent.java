package rs.devana.labs.studentinfoapp.infrastructure.event_bus_events;

public class NotificationsFetchedEvent {

    String notifications;

    public NotificationsFetchedEvent(String notifications){
        this.notifications = notifications;
    }

    public String getNotifications() {
        return notifications;
    }

    public void setNotifications(String notifications) {
        this.notifications = notifications;
    }
}
