public class Grade implements Comparable<Grade>, Cloneable{
    private Double partialScore = 0.0, examScore = 0.0;
    private Student student;
    private String course;

    public Grade(Student student, String course){
        this.student = student;
        this.course = course;
    }
    public Double getPartialScore() {
        return partialScore;
    }

    public void setPartialScore(Double partialScore) {
        this.partialScore = partialScore;
    }

    public Double getExamScore() {
        return examScore;
    }

    public void setExamScore(Double examScore) {
        this.examScore = examScore;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public double getTotal(){
        return partialScore + examScore;
    }
    @Override
    public int compareTo(Grade o) {
        return Double.compare(this.getTotal(), o.getTotal());
    }
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
    @Override
    public String toString() {
        return student + " - Partial Score: " + partialScore + " Exam Score: " + examScore;
    }
}
