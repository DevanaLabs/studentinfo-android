package rs.devana.labs.studentinfoapp.infrastructure.event_bus_events;

import rs.devana.labs.studentinfoapp.domain.models.event.Event;

public class OpenEventActivityEvent {
    private Event event;

    public OpenEventActivityEvent(Event event) {

        this.event = event;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
}
