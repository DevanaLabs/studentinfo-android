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

    @Override
    public String toString() {
        return "id=" + id +
                ", floor=" + floor +
                ", name='" + name + '\'' +
                ", lectures=" + lectures;
    }
}
