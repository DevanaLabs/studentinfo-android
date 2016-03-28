package rs.devana.labs.studentinfoapp.domain.models.lecture;

import java.util.List;

import rs.devana.labs.studentinfoapp.domain.models.classroom.Classroom;
import rs.devana.labs.studentinfoapp.domain.models.course.Course;
import rs.devana.labs.studentinfoapp.domain.models.notification.lecture.LectureNotification;

public class Lecture {

    private int id, type;
    private String teacher;
    private int startsAt, endsAt;
    private Course course;
    private Classroom classroom;
    private List<LectureNotification> lectureNotifications;
    private static final int secondsInHour = 60*60;
    private static final int secondsInDay = 24*secondsInHour;

    public Lecture(int id, int type, Course course, int startsAt, int endsAt, String teacher, Classroom classroom, List<LectureNotification> lectureNotifications) {
        this.id = id;
        this.type = type;
        this.startsAt = startsAt;
        this.endsAt = endsAt;
        this.course = course;
        this.teacher = teacher;
        this.classroom = classroom;
        this.lectureNotifications = lectureNotifications;
    }

    public int getId() {
        return id;
    }

    public String getLectureName(){
        return course.getName() + " " + teacher + " " + type;
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

    public String getTypeString() {
        switch (type) {
            case 1:
                return "Вежбе";
            case 2:
                return "Предавања и вежбе";
            case 3:
                return "Практикум";
            default:
                return "Предавање";
        }
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStartsAt() {
        return startsAt;
    }

    public void setStartsAt(int startsAt) {
        this.startsAt = startsAt;
    }

    public int getEndsAt() {
        return endsAt;
    }

    public void setEndsAt(int endsAt) {
        this.endsAt = endsAt;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Classroom getClassroom() {
        return classroom;
    }

    public void setClassroom(Classroom classroom) {
        this.classroom = classroom;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public List<LectureNotification> getLectureNotifications() {
        return lectureNotifications;
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
