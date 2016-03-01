package rs.devana.labs.studentinfo.infrastructure.json.parser;

import org.json.JSONObject;

import javax.inject.Inject;

import rs.devana.labs.studentinfo.domain.models.event.Event;

public class EventParser {
    @Inject
    public EventParser(){
    }

    public Event parse(JSONObject jsonEvent){
        return null;
    }
}
