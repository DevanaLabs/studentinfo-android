package rs.devana.labs.studentinfo.infrastructure.event_bus_events;

public class GroupsFetchedEvent {
    private String groupsJSON;

    public GroupsFetchedEvent(String groupsJSON) {
        this.groupsJSON = groupsJSON;
    }

    public String getGroupsJSON() {
        return groupsJSON;
    }

    public void setGroupsJSON(String groupsJSON) {
        this.groupsJSON = groupsJSON;
    }
}
