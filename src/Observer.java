public interface Observer {
    void update(Notification notification);
}
class Notification{
    String message = "NOTIFICARE!";
    Grade grade;
    public Notification(String message, Grade grade){
        this.grade = grade;
        this.message = message;
    }
    @Override
    public String toString() {
        return message;
    }
}