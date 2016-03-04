package rs.devana.labs.studentinfo.domain.models.course;

import java.util.List;

import rs.devana.labs.studentinfo.domain.models.lecture.Lecture;

public class Course {

    private int id, semester, espb;
    private String name;
    private List<Lecture> lectures;

    public Course(int id, String name, int semester, int espb, List<Lecture> lectures){
        this.id = id;
        this.name = name;
        this.semester = semester;
        this.espb = espb;
        this.lectures = lectures;
    }

    public int getId() {
        return id;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public int getEspb() {
        return espb;
    }

    public void setEspb(int espb) {
        this.espb = espb;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Lecture> getLectures() {
        return lectures;
    }

    public void setLectures(List<Lecture> lectures) {
        this.lectures = lectures;
    }

    @Override
    public String toString() {
        return "id=" + id +
                ", semester=" + semester +
                ", espb=" + espb +
                ", name='" + name + '\'' +
                ", lectures=" + lectures;
    }
}
