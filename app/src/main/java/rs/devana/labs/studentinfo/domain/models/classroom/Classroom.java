package rs.devana.labs.studentinfo.domain.models.classroom;

import java.util.List;

import rs.devana.labs.studentinfo.domain.models.lecture.Lecture;

public class Classroom {

    private int id, floor;
    private String name;
    private List<Lecture> lectures;

    public Classroom(int id, String name, int floor, List<Lecture> lectures){
        this.id = id;
        this.name = name;
        this.floor = floor;
        this.lectures = lectures;
    }

    public int getId() {
        return id;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
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
                ", floor=" + floor +
                ", name='" + name + '\'' +
                ", lectures=" + lectures;
    }
}
