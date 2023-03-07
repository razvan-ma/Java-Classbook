import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Catalog implements Subject {
    private static Catalog obj = null;
    private ArrayList<Course> courses;
    public ArrayList<Course> getCourses() {
        return courses;
    }

    public void setCourses(ArrayList<Course> courses) {
        this.courses = courses;
    }
    public ArrayList<Observer> observers = new ArrayList<>();
    private Catalog() {
        courses = new ArrayList<Course>();
    }
    public static Catalog getInstance(){
        if(obj == null)
            obj = new Catalog();
        return obj;
    }
    public void addCourse(Course course){
        courses.add(course);
    }
    public void removeCourse(Course course){
        courses.remove(course);
    }
    @Override
    public void addObserver(Observer observer) {
        for (Observer observer1: observers) {
            if(((Parent)observer1).getFirstName().equals(((Parent)observer).getFirstName())
                && ((Parent)observer1).getLastName().equals(((Parent)observer).getLastName()))
                    return;
        }
        observers.add(observer);
    }
    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(Grade grade) {
        for (Observer observer: observers) {
            observer.update(new Notification(grade.getStudent() + " - " +
                    grade.getCourse() + " - Nota: " + grade.getTotal(), grade));
        }
    }
}