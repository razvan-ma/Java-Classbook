import java.util.ArrayList;
import java.util.Comparator;

public class Group extends ArrayList<Student> {
    private Assistant assistant;
    private String ID;
    private Comparator<Student> comparator;
    public Assistant getAssistant() {
        return assistant;
    }
    public void setAssistant(Assistant assistant) {
        this.assistant = assistant;
    }
    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
    public Comparator<Student> getComparator() {
        return comparator;
    }
    public void setComparator(Comparator<Student> comparator) {
        this.comparator = comparator;
    }
    public Group(String ID, Assistant assistant) {
        this.ID = ID;
        this.assistant = assistant;
    }
    public Group(String ID, Assistant assistant, Comparator<Student> comp) {
        this.ID = ID;
        this.assistant = assistant;
        comp = new Comparator<Student>() {
            @Override
            public int compare(Student o1, Student o2) {
                return o1.getLastName().compareTo(o2.getLastName());
            }
        };
        setComparator(comp);
    }
}
