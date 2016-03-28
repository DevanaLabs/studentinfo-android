package rs.devana.labs.studentinfoapp.infrastructure.event_bus_events;

public class EventsFetchedEvent {
    private String groups;

    public EventsFetchedEvent(String groupsJSON) {
        this.groups = groupsJSON;
    }

    public String getGroups() {
        return groups;
    }

    public void setGroups(String groups) {
        this.groups = groups;
    }
}
