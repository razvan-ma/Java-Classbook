public abstract class User {
    private String firstName, lastName;
    public User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    @Override
    public String toString() {
        return firstName + " " + lastName;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof User user))
            return false;
        else {
            return user.firstName.equals(this.firstName) && user.lastName.equals(this.lastName);
        }
    }
}
class Student extends User {
    private Parent mother, father;
    public Student(String firstName, String lastName) {
        super(firstName, lastName);
    }
    public void setFather(Parent father) {
        this.father = father;
    }
    public void setMother(Parent mother) {
        this.mother = mother;
    }

    public Parent getMother() {
        return mother;
    }

    public Parent getFather() {
        return father;
    }
}
class Parent extends User implements Observer{
    public Parent(String firstName, String lastName) {
        super(firstName, lastName);
    }
    Notification notification;
    ParentPage parentPage;
    @Override
    public void update(Notification notification) {
        if(notification.grade.getStudent().getLastName().equals(getLastName())) {
            this.notification = notification;
        }
    }
}
class Teacher extends User implements Element{
    public Teacher(String firstName, String lastName) {
        super(firstName, lastName);
    }

    @Override
    public void accept(Visitor visitor){
        visitor.visit(this);
    }
}
class Assistant extends User implements Element{
    public Assistant(String firstName, String lastName) {
        super(firstName, lastName);
    }

    @Override
    public void accept(Visitor visitor){
        visitor.visit(this);
    }
}
class UserFactory{
    public User getUser(String type, String firstName, String lastName) {
        User user = null;
        if (type.equals("Student"))
            user = new Student(firstName, lastName);
        if (type.equals("Parent"))
            user = new Parent(firstName, lastName);
        if (type.equals("Teacher"))
            user = new Teacher(firstName, lastName);
        if (type.equals("Assistant"))
            user = new Assistant(firstName, lastName);
        return user;
    }
}
