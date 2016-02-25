package rs.devana.labs.studentinfo.domain.models.lecture;

import java.util.Calendar;
import java.util.List;

import rs.devana.labs.studentinfo.domain.models.classroom.Classroom;
import rs.devana.labs.studentinfo.domain.models.course.Course;
import rs.devana.labs.studentinfo.domain.models.notification.lecture.LectureNotification;

public class Lecture {
    private int id;
    private String type, teacher;
    private int startsAt, endsAt;
    private Course course;
    private Classroom classroom;
    private List<LectureNotification> lectureNotifications;

    public  Lecture(int id, String type, Course course, int startsAt, int  endsAt, String teacher, Classroom classroom, List<LectureNotification> lectureNotifications){
        this.id = id;
        this.type = type;
        this.startsAt = startsAt;
        this.endsAt = endsAt;
        this.course = course;
        this.teacher = teacher;
        this.classroom = classroom;
        this.lectureNotifications = lectureNotifications;
    }

    @Override
    public String toString() {
        return "id=" + id +
                ", type='" + type + '\'' +
                ", course='" + course + '\'' +
                ", teacher='" + teacher + '\'' +
                ", startsAt='" + startsAt + '\'' +
                ", endsAt='" + endsAt + '\'' +
                ", classroom='" + classroom + '\'' +
                ", notifications='" + lectureNotifications+ '\'';
    }
}
