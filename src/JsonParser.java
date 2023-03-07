import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
public class JsonParser {
    private static JsonParser obj = null;
    public static JsonParser getInstance(){
        if(obj == null)
            obj = new JsonParser();
        return obj;
    }
    public ScoreVisitor scoreVisitor = new ScoreVisitor();
    public void parse() throws IOException, ParseException {

        JSONObject jo = (JSONObject) new JSONParser().parse(new FileReader("catalog.json"));
        Object coursesObject = jo.get("courses");
        JSONArray courses = (JSONArray) coursesObject;

        UserFactory factory = new UserFactory();
        for (Object courseObject :courses) {
            JSONObject courseJson = (JSONObject) courseObject;

            String courseName = (String) courseJson.get("name");
            String type = (String) courseJson.get("type");
            String strategy = (String) courseJson.get("strategy");
            JSONObject teacherJson = (JSONObject) courseJson.get("teacher");
            String teacherFirstName = (String) teacherJson.get("firstName");
            String teacherLastName = (String) teacherJson.get("lastName");

            JSONArray assistantsJson = (JSONArray) courseJson.get("assistants");
            Set<Assistant> assistantSet = new HashSet<>();
            for (Object assistant: assistantsJson) {
                String firstName = (String) (((JSONObject) assistant).get("firstName"));
                String lastName = (String) (((JSONObject) assistant).get("lastName"));
                assistantSet.add(new Assistant(firstName,lastName));
            }

            JSONArray groupsJson = (JSONArray) courseJson.get("groups");
            HashMap<String,Group> groupHashMap = new HashMap<>();
            for (Object groupObject : groupsJson) {
                Group group;
                String ID = (String) (((JSONObject)groupObject).get("ID"));
                JSONObject assistantJson = (JSONObject) ((JSONObject) groupObject).get("assistant");
                String assistantFirstName = (String) assistantJson.get("firstName");
                String assistantLastName = (String) assistantJson.get("lastName");

                Assistant groupAssistant = null;
                for (Assistant assistant: assistantSet) {
                    if (assistant.getFirstName().equals(assistantFirstName) && assistant.getLastName().equals(assistantLastName))
                        groupAssistant = assistant;
                }
                assert groupAssistant != null;

                group = new Group(ID, groupAssistant);
                JSONArray studentsJson = (JSONArray) ((JSONObject) groupObject).get("students");
                for (Object studentObject : studentsJson) {
                    String firstName = (String) ((JSONObject) studentObject).get("firstName");
                    String lastName = (String) ((JSONObject) studentObject).get("lastName");


                    Student student = (Student) factory.getUser("Student", firstName, lastName);

                    String motherFirstName;
                    String motherLastName;
                    JSONObject motherJson = (JSONObject) ((JSONObject) studentObject).get("mother");

                    if(motherJson != null) {
                        Parent mother;
                        motherFirstName = (String) motherJson.get("firstName");
                        motherLastName = (String) motherJson.get("lastName");
                        mother = (Parent) factory.getUser("Parent", motherFirstName, motherLastName);
                        student.setMother(mother);
                        Catalog.getInstance().addObserver(mother);
                    }

                    String fatherFirstName;
                    String fatherLastName;

                    JSONObject fatherJson = (JSONObject) ((JSONObject) studentObject).get("father");
                    if(fatherJson != null) {
                        Parent father;
                        fatherFirstName = (String) fatherJson.get("firstName");
                        fatherLastName = (String) fatherJson.get("lastName");
                        father = (Parent) factory.getUser("Parent", fatherFirstName, fatherLastName);
                        student.setFather(father);
                        Catalog.getInstance().addObserver(father);
                    }
                    group.add(student);
                }
                groupHashMap.put(ID,group);
            }
            if(type.equals("FullCourse")) {
                Course fullCourse = new FullCourse.FullCourseBuilder(courseName)
                        .set(assistantSet)
                        .teacher(new Teacher(teacherFirstName, teacherLastName))
                        .hashMap(groupHashMap)
                        .strategy(strategy)
                        .build();
                Catalog.getInstance().addCourse(fullCourse);
            }
            if(type.equals("PartialCourse")) {
                Course partialCourse = new PartialCourse.PartialCourseBuilder(courseName)
                        .set(assistantSet)
                        .teacher(new Teacher(teacherFirstName, teacherLastName))
                        .hashMap(groupHashMap)
                        .strategy(strategy)
                        .build();
                Catalog.getInstance().addCourse(partialCourse);
            }
        }
        Object examGradesObject = jo.get("examScores");
        JSONArray examScores = (JSONArray) examGradesObject;
        for (Object examGradeObject: examScores)  {

            JSONObject examGradeJson = (JSONObject) examGradeObject;
            JSONObject teacherJson = (JSONObject) examGradeJson.get("teacher");
            String teacherFirstName = (String) teacherJson.get("firstName");
            String teacherLastName = (String) teacherJson.get("lastName");

            JSONObject studentJson = (JSONObject) examGradeJson.get("student");
            String studentFirstName = (String) studentJson.get("firstName");
            String studentLastName = (String) studentJson.get("lastName");

            String courseName = (String) examGradeJson.get("course");

            Number grade = (Number) examGradeJson.get("grade");

            Teacher teacher = (Teacher) factory.getUser("Teacher", teacherFirstName, teacherLastName);
            Student student = (Student) factory.getUser("Student", studentFirstName, studentLastName);
            boolean teacherExists = false;
            for (Teacher mapTeacher: scoreVisitor.getExamScores().keySet()) {
                if(mapTeacher.equals(teacher)) {
                    scoreVisitor.addExamGrade(mapTeacher, student, courseName, grade.doubleValue());
                    teacherExists = true;
                }
            }
            if(!teacherExists)
                scoreVisitor.addExamGrade(teacher, student, courseName, grade.doubleValue());
        }
        Object partialGradesObject = jo.get("partialScores");
        JSONArray partialScores = (JSONArray) partialGradesObject;
        for (Object partialGradeObject: partialScores)  {

            JSONObject partialGradeJson = (JSONObject) partialGradeObject;
            JSONObject teacherJson = (JSONObject) partialGradeJson.get("assistant");
            String assistantFirstName = (String) teacherJson.get("firstName");
            String assistantLastName = (String) teacherJson.get("lastName");

            JSONObject studentJson = (JSONObject) partialGradeJson.get("student");
            String studentFirstName = (String) studentJson.get("firstName");
            String studentLastName = (String) studentJson.get("lastName");

            String courseName = (String) partialGradeJson.get("course");

            Number grade = (Number) partialGradeJson.get("grade");
            // ia vezi cu asta
            Assistant assistant = (Assistant) factory.getUser("Assistant", assistantFirstName, assistantLastName);
            Student student = (Student) factory.getUser("Student", studentFirstName, studentLastName);
            boolean assistantExists = false;
            for (Assistant mapAssistant: scoreVisitor.getPartialScores().keySet()) {
                if(mapAssistant.equals(assistant)) {
                    scoreVisitor.addPartialGrade(mapAssistant, student, courseName, grade.doubleValue());
                    assistantExists = true;
                }
            }
            if(!assistantExists)
                scoreVisitor.addPartialGrade(assistant, student, courseName, grade.doubleValue());
        }
    }

    public static void main(String[] args) throws IOException, ParseException {
        JsonParser.getInstance().parse();
        for (Course course:Catalog.getInstance().getCourses()) {
            JsonParser.getInstance().scoreVisitor.visit(course.getTeacher());
            for (Assistant assistant: course.assistants) {
                JsonParser.getInstance().scoreVisitor.visit(assistant);
            }
        }
        Course testCourse = Catalog.getInstance().getCourses().get(0);
        testCourse.makeBackup();
        System.out.println(testCourse.grades);
        testCourse.grades.remove(0);
        testCourse.grades.remove(0);
        testCourse.grades.remove(0);
        System.out.println(testCourse.grades);
        testCourse.undo();
        System.out.println(testCourse.grades);
    }
}
