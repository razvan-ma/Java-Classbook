import java.util.*;

public abstract class Course{
    private String name;
    private Teacher teacher;
    public Set<Assistant> assistants;
    public ArrayList<Grade> grades;
    public HashMap<String, Group> groupDictionary;
    private int credits;
    private String strategy;


    public Course(CourseBuilder builder){
        name = builder.name;
        teacher = builder.teacher;
        assistants = builder.assistants;
        grades = builder.grades;
        groupDictionary = builder.groupDictionary;
        strategy = builder.strategy;
    }
    @Override
    public String toString() {
        return name;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }


    public void addAssistant(String ID, Assistant assistant){
        Group group = groupDictionary.get(ID);
        group.setAssistant(assistant);
        assistants.add(assistant);
    }

    public void addStudent(String ID, Student student){
        Group group = groupDictionary.get(ID);
        group.add(student);
    }

    public void addGroup(Group group){
        groupDictionary.put(group.getID(), group);
    }

    public void addGroup(String ID, Assistant assistant){
        Group g = new Group(ID, assistant);
        groupDictionary.put(ID, g);
    }

    public void addGroup(String ID, Assistant assistant, Comparator<Student> comp){
        Group g = new Group(ID, assistant, comp);
        groupDictionary.put(ID, g);
    }
    public Grade getGrade(Student student){
        for (Grade grade: grades) {
            if(grade.getStudent().equals(student))
                return grade;
        }
        return null;
    }
    public void addGrade(Grade grade){
        grades.add(grade);
    }

    public ArrayList<Student> getAllStudents(){
        ArrayList<Student> students = new ArrayList<>();
        Collection<Group> groups = groupDictionary.values();
        for (Group group : groups) {
            students.addAll(group);
        }
        return students;
    }
    public HashMap<Student, Grade> getAllStudentGrades(){
        HashMap<Student,Grade> studentGrades = new HashMap<>();
        for (Grade grade: grades) {
            studentGrades.put(grade.getStudent(),grade);
        }
        return studentGrades;
    }
    public abstract ArrayList<Student> getGraduatedStudents();
    public abstract static class CourseBuilder{
        private String name;
        private Teacher teacher;
        private Set<Assistant> assistants;
        private ArrayList<Grade> grades;
        private HashMap<String, Group> groupDictionary;

        private int credits;
        private String strategy;
        public CourseBuilder(String name){
            this.name = name;
            grades = new ArrayList<>();
            assistants = new HashSet<>();
            groupDictionary = new HashMap<>();
        }
        public CourseBuilder teacher(Teacher teacher){
            this.teacher = teacher;
            return this;
        }
        public CourseBuilder grades(ArrayList<Grade> grades){
            this.grades = grades;
            return this;
        }
        public CourseBuilder set(Set<Assistant> assistants){
            this.assistants = assistants;
            return this;
        }
        public CourseBuilder hashMap(HashMap<String, Group> dictionary){
            this.groupDictionary = dictionary;
            return this;
        }
        public CourseBuilder strategy(String strategy){
            this.strategy = strategy;
            return this;
        }

        public abstract Course build();
    }
    public String getStrategy() {
        return strategy;
    }

    public void setStrategy(String strategy) {
        this.strategy = strategy;
    }

    public Student getBestStudent(){
        Strategy s = null;
        if(strategy.equals("BestPartialScore"))
            s = new BestPartialScore();
        if(strategy.equals("BestExamScore"))
            s = new BestExamScore();
        if(strategy.equals("BestTotalScore"))
            s = new BestTotalScore();
        assert s != null;
        return s.decide(this);
    }
    private Snapshot snapshot = new Snapshot();
    public void makeBackup(){
        snapshot.grades = new ArrayList<>();
        snapshot.grades.addAll(grades);
    }
    public void undo(){
        if(!snapshot.grades.isEmpty())
            grades = snapshot.grades;
    }
    private class Snapshot{
        ArrayList<Grade> grades;
    }

}
class PartialCourse extends Course{
    public PartialCourse(CourseBuilder builder) {
        super(builder);
    }

    public static class PartialCourseBuilder extends CourseBuilder{

        public PartialCourseBuilder(String name) {
            super(name);
        }
        @Override
        public Course build() {
            return new PartialCourse(this);
        }
    }
    @Override
    public ArrayList<Student> getGraduatedStudents() {
        ArrayList<Student> graduatedStudents = new ArrayList<>();
        HashMap<Student, Grade> studentGrades = getAllStudentGrades();
        Set<Student> students = studentGrades.keySet();
        for (Student student: students) {
            if(studentGrades.get(student).getTotal() >= 5)
                graduatedStudents.add(student);
        }
        return graduatedStudents;
    }
}
class FullCourse extends Course{
    public FullCourse(CourseBuilder builder) {
        super(builder);
    }
    public static class FullCourseBuilder extends CourseBuilder{

        public FullCourseBuilder(String name) {
            super(name);
        }
        @Override
        public Course build() {
            return new FullCourse(this);
        }
    }
    @Override
    public ArrayList<Student> getGraduatedStudents() {
        ArrayList<Student> graduatedStudents = new ArrayList<>();
        HashMap<Student, Grade> studentGrades = getAllStudentGrades();
        Set<Student> students = studentGrades.keySet();
        for (Student student: students) {
            if(studentGrades.get(student).getExamScore() >= 2 && studentGrades.get(student).getPartialScore() >= 3)
                graduatedStudents.add(student);
        }
        return graduatedStudents;
    }
}