package rs.devana.labs.studentinfoapp.infrastructure.event_bus_events;

import rs.devana.labs.studentinfoapp.domain.models.lecture.Lecture;

public class OpenLectureFragmentEvent {
    private Lecture lecture;

    public OpenLectureFragmentEvent(Lecture lecture) {

        this.lecture = lecture;
    }

    public Lecture getLecture() {
        return lecture;
    }

    public void setLecture(Lecture lecture) {
        this.lecture = lecture;
    }
}
