package rs.devana.labs.studentinfo.domain.models.event;

import org.json.JSONObject;

import java.util.List;

public interface EventRepositoryInterface {
    List<Event> getEvents();

    Event getEvent(JSONObject jsonEvent);
}
