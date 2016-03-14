package rs.devana.labs.studentinfo.infrastructure.event_bus_events;

public class ScheduleFetchedEvent {
    private String scheduleJSON;

    public ScheduleFetchedEvent(String scheduleJSON) {

        this.scheduleJSON = scheduleJSON;
    }

    public String getScheduleJSON() {
        return scheduleJSON;
    }

    public void setScheduleJSON(String scheduleJSON) {
        this.scheduleJSON = scheduleJSON;
    }
}
