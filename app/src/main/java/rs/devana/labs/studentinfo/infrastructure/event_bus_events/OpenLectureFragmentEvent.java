package rs.devana.labs.studentinfo.infrastructure.event_bus_events;

import rs.devana.labs.studentinfo.domain.models.lecture.Lecture;

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
