import org.json.simple.parser.ParseException;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
public class StudentPage extends JFrame {

    JList<Course> courseJList;
    DefaultListModel<Course> courseListModel;
    public StudentPage(String name, Student student){
        super(name);
        setMinimumSize(new Dimension(800,800));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel listPanel = new JPanel();
        JPanel studentPanel = new JPanel();
        JPanel coursePanel = new JPanel();

        JLabel nameText = new JLabel("Student: " + student.getFirstName() + " " + student.getLastName());
        studentPanel.add(nameText);
        studentPanel.setBounds(50,50,150,50);


        coursePanel.setBackground(new Color(200,200,200));
        coursePanel.setBounds(400,100,350,80);
        coursePanel.setLayout(new BoxLayout(coursePanel,BoxLayout.Y_AXIS));

        JLabel courseText = new JLabel();
        JLabel teacherText = new JLabel();
        JLabel assistantsText = new JLabel();
        JLabel assistantText = new JLabel();

        JLabel partialText = new JLabel();
        JLabel examText = new JLabel();



        coursePanel.add(courseText);
        coursePanel.add(teacherText);
        coursePanel.add(assistantsText);
        coursePanel.add(assistantText);
        coursePanel.add(partialText);
        coursePanel.add(examText);

        ListSelectionListener listSelectionListener = new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if(e.getSource() instanceof JList<?>) {
                    Course courseObject = (Course) ((JList<?>) e.getSource()).getSelectedValue();
                    JsonParser.getInstance().scoreVisitor.visit(courseObject.getTeacher());
                    courseText.setText("Course: " + courseObject.getName());
                    teacherText.setText("Teacher: " + courseObject.getTeacher());
                    String assistantString = "Assistants: ";
                    int i = 0;
                    Assistant yourAssistant = null;
                    for (Assistant assistant : courseObject.assistants) {
                        JsonParser.getInstance().scoreVisitor.visit(assistant);
                        if(i > 0) {
                            assistantString = assistantString + ", " + assistant.toString();
                        }
                        else
                            assistantString = assistantString + " " + assistant.toString();
                        i++;
                        for (Group group : courseObject.groupDictionary.values()) {
                            if(group.getAssistant().equals(assistant) && group.contains(student)){
                                yourAssistant = assistant;
                            }
                        }
                    }
                    assistantsText.setText(assistantString);
                    assistantText.setText("Your Assistant: " + yourAssistant.toString());
                    examText.setText("Grade: " + courseObject.getGrade(student).getTotal());
                }
            }
        };
        courseListModel = new DefaultListModel<>();
        courseJList = new JList<>(courseListModel);
        courseJList.addListSelectionListener(listSelectionListener);
        courseJList.setFixedCellWidth(300);
        courseJList.setFixedCellHeight(100);
        for (Course course: Catalog.getInstance().getCourses()) {
            ArrayList<Student> courseStudents = course.getAllStudents();
            for (Student courseStudent: courseStudents) {
                if(courseStudent.equals(student))
                    courseListModel.addElement(course);
            }
        }
        listPanel.setBounds(0,100,300,400);
        listPanel.add(courseJList);


        setLayout(null);
        add(listPanel);
        add(studentPanel);
        add(coursePanel);

        setVisible(true);
    }
    public static void main(String[] args) throws IOException, ParseException {
        JsonParser.getInstance().parse();
        StudentPage studentPage = new StudentPage("Student Page", new Student("Gigel","Frone"));
    }
}
