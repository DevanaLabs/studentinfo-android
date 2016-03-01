package rs.devana.labs.studentinfo.infrastructure.repository;

import org.json.JSONObject;

import java.util.List;

import rs.devana.labs.studentinfo.domain.models.event.Event;
import rs.devana.labs.studentinfo.domain.models.event.EventRepositoryInterface;

public class EventRepository implements EventRepositoryInterface {
    @Override
    public List<Event> getEvents() {
        return null;
    }
}
