import java.util.*;

public interface Visitor {
    void visit(Assistant assistant);
    void visit(Teacher teacher);
}
class ScoreVisitor implements Visitor{
    private HashMap<Teacher, ArrayList<Tuple<Student,String,Double>>> examScores;
    private HashMap<Assistant, ArrayList<Tuple<Student,String,Double>>> partialScores;
    public HashMap<Teacher, ArrayList<Tuple<Student, String, Double>>> getExamScores() {
        return examScores;
    }

    public HashMap<Assistant, ArrayList<Tuple<Student, String, Double>>> getPartialScores() {
        return partialScores;
    }


    ScoreVisitor(){
        examScores = new HashMap<>();
        partialScores = new HashMap<>();
    }
    public void addExamGrade(Teacher teacher,Student student, String course, Double grade){
        if(!examScores.containsKey(teacher)) {
            examScores.put(teacher, new ArrayList<>());
            examScores.get(teacher).add(new Tuple<>(student,course,grade));
        }
        else{
            examScores.get(teacher).add(new Tuple<>(student,course,grade));
        }
    }
    public void addPartialGrade(Assistant assistant,Student student, String course, Double grade){
        if(!partialScores.containsKey(assistant)) {
            partialScores.put(assistant, new ArrayList<>());
            partialScores.get(assistant).add(new Tuple<>(student,course,grade));
        }
        else{
            partialScores.get(assistant).add(new Tuple<>(student,course,grade));
        }
    }
    @Override
    public void visit(Assistant assistant){
        for (Assistant mapAssistant: partialScores.keySet()) {
            if (assistant.equals(mapAssistant))
                assistant = mapAssistant;
        }
        for (Tuple<Student,String,Double> gradeTuple: partialScores.get(assistant)) {
            for (Course course : Catalog.getInstance().getCourses()) {
                if(course.getName().equals(gradeTuple.n)) {
                    boolean found = false;
                    if(course.grades != null) {
                        for (Grade grade:course.grades) {
                            // if the student already has a grade in this course, via visit(teacher),
                            // just set the partial score
                            if (grade.getStudent().getFirstName().equals(gradeTuple.s.getFirstName())
                                    && grade.getStudent().getFirstName().equals(gradeTuple.s.getFirstName())) {
                                grade.setPartialScore(gradeTuple.d);
                                Catalog.getInstance().notifyObservers(grade);
                                found = true;
                            }
                        }
                    }
                    // if the student does not have a grade for this course, create a new grade object
                    if(!found) {
                        Grade grade = new Grade(gradeTuple.s, gradeTuple.n);
                        grade.setPartialScore(gradeTuple.d);
                        course.addGrade(grade);
                        Catalog.getInstance().notifyObservers(grade);
                    }
                }
            }
        }

    }
    @Override
    public void visit(Teacher teacher){
        for (Teacher mapTeacher: examScores.keySet()) {
            if (teacher.equals(mapTeacher))
                teacher = mapTeacher;
        }
        for (Tuple<Student,String,Double> gradeTuple: examScores.get(teacher)) {
            for (Course course : Catalog.getInstance().getCourses()) {
                if(course.getName().equals(gradeTuple.n)) {
                    if(course.grades != null) {
                        if(course.getGrade(gradeTuple.s) != null){
                            course.getGrade(gradeTuple.s).setExamScore(gradeTuple.d);
                        }
                        else {
                            // if the student does not have a grade for this course, create a new grade object
                            Grade grade = new Grade(gradeTuple.s, gradeTuple.n);
                            grade.setExamScore(gradeTuple.d);
                            course.addGrade(grade);
                        }
                    }
                }
            }
        }
    }
    private class Tuple<S,N,D>{
        S s;
        N n;
        D d;
        public Tuple(S s, N n, D d) {
            this.s = s;
            this.n = n;
            this.d = d;
        }
        @Override
        public String toString() {
            return "Tuple{" +
                    "s=" + s +
                    ", n=" + n +
                    ", d=" + d +
                    '}';
        }
    }
}
