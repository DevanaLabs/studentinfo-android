package rs.devana.labs.studentinfoapp.domain.models.group;

public class Group {

    public int id, year;
    public String name;

    public Group(int id, int year, String name){
        this.id = id;
        this.year = year;
        this.name = name;
    }

    @Override
    public String toString() {
        return "id=" + id +
                ", year=" + year +
                ", name='" + name + '\'';
    }
}
