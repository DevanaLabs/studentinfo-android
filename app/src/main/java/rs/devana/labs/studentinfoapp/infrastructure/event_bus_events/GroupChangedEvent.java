package rs.devana.labs.studentinfoapp.infrastructure.event_bus_events;

public class GroupChangedEvent {
    public final String group;

    public GroupChangedEvent(String group) {
        this.group = group;
    }
}
