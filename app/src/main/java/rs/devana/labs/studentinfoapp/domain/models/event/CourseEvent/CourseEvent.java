package rs.devana.labs.studentinfoapp.domain.models.event.CourseEvent;

import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;


import rs.devana.labs.studentinfoapp.domain.models.classroom.Classroom;
import rs.devana.labs.studentinfoapp.domain.models.course.Course;
import rs.devana.labs.studentinfoapp.domain.models.event.Event;
import rs.devana.labs.studentinfoapp.domain.models.notification.event.EventNotification;

public class CourseEvent extends Event {

    protected Course course;

    @Inject
    public CourseEvent() {

    }

    public CourseEvent(int id, String type, String description, Course course, Calendar startsAt, Calendar endsAt, List<Classroom> classrooms, List<EventNotification> eventNotifications) {
        super(id, type, description, startsAt, endsAt, classrooms, eventNotifications);
        this.course = course;
    }
}
