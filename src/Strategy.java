public abstract class Strategy {

    abstract Student decide(Course course);
}
class BestPartialScore extends Strategy {
    @Override
    public Student decide(Course course) {

        double max = course.grades.get(0).getPartialScore();
        Student bestStudent = null;

        for (Grade grade: course.grades) {
            if(max < grade.getPartialScore()) {
                max = grade.getPartialScore();
                bestStudent = grade.getStudent();
            }
        }
        return bestStudent;
    }
}
class BestExamScore extends Strategy{
    @Override
    public Student decide(Course course) {
        double max = course.grades.get(0).getExamScore();
        Student bestStudent = null;

        for (Grade grade: course.grades) {
            if(max < grade.getExamScore()) {
                max = grade.getExamScore();
                bestStudent = grade.getStudent();
            }
        }
        return bestStudent;
    }
}
class BestTotalScore extends Strategy{
    @Override
    public Student decide(Course course) {
        double max = course.grades.get(0).getTotal();
        Student bestStudent = null;

        for (Grade grade: course.grades) {
            if(max < grade.getTotal()) {
                max = grade.getTotal();
                bestStudent = grade.getStudent();
            }
        }
        return bestStudent;
    }
}