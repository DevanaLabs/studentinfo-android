package rs.devana.labs.studentinfo.infrastructure.repository;

import org.json.JSONArray;

import javax.inject.Inject;

import rs.devana.labs.studentinfo.domain.api.ApiDataFetch;
import rs.devana.labs.studentinfo.domain.models.event.EventRepositoryInterface;
import rs.devana.labs.studentinfo.infrastructure.json.parser.EventParser;

public class EventRepository implements EventRepositoryInterface {
    ApiDataFetch apiDataFetch;
    EventParser eventParser;

    @Inject
    public EventRepository(ApiDataFetch apiDataFetch, EventParser eventParser) {
        this.apiDataFetch = apiDataFetch;
        this.eventParser = eventParser;
    }

    @Override
    public JSONArray getEvents() {
        return apiDataFetch.getAllEvents();
    }
}
