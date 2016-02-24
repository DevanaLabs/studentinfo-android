package rs.devana.labs.studentinfo.domain.models.lecture;

public class Lecture {
    private int id;
    private String type, course, teacher, classroom;

    public  Lecture(int id, String type, String course, String teacher, String classroom){
        this.id = id;
        this.type = type;
        this.course = course;
        this.teacher = teacher;
        this.classroom = classroom;
    }

    @Override
    public String toString() {
        return "id=" + id +
                ", type='" + type + '\'' +
                ", course='" + course + '\'' +
                ", teacher='" + teacher + '\'' +
                ", classroom='" + classroom + '\'';
    }
}
