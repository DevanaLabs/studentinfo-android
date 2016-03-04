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
    private static final int secondsInHour = 60*60;
    private static final int secondsInDay = 24*secondsInHour;

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

    public String getLectureName(){
        return this.course.getName();
    }

    public String getLectureClassroom(){
        return this.classroom.getName();
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

    public int getStartsAt() {
        return convertTime(startsAt);
    }

    public void setStartsAt(int startsAt) {
        this.startsAt = startsAt;
    }

    public int getEndsAt() {
        return convertTime(endsAt);
    }

    public void setEndsAt(int endsAt) {
        this.endsAt = endsAt;
    }

    public String getConvertedStartsAt(){
        StringBuilder result = new StringBuilder();
        int convertedTime = convertTime(startsAt);
        // Turning one digit time into two digit e.g. 9 -> 09
        if (convertedTime < 10){
            result.append('0');
        }
        return result.append(convertedTime).toString();
    }

    public String getConvertedEndsAt(){
        StringBuilder result = new StringBuilder();
        int convertedTime = convertTime(endsAt);
        // Turning one digit time into two digit e.g. 9 -> 09
        if (convertedTime < 10){
            result.append('0');
        }
        return result.append(convertedTime).toString();
    }

    private int convertTime(int seconds){
        return ((seconds%secondsInDay)/secondsInHour);
    }
}
