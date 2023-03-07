import org.json.simple.parser.ParseException;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Teacher_AssistantPage extends JFrame {
    JsonParser parser = new JsonParser();
    JList<Course> courseJList;
    DefaultListModel<Course> courseListModel;
    ArrayList <JLabel> gradesText;
    JButton button;

    public Teacher_AssistantPage(String name, User user){

        super(name);
        setMinimumSize(new Dimension(800,800));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel listPanel = new JPanel();
        JPanel userPanel = new JPanel();
        JPanel gradesPanel = new JPanel();

        JLabel nameText = new JLabel("User: "+ user.getFirstName() + " " + user.getLastName());
        userPanel.add(nameText);
        userPanel.setBounds(50,50,150,50);


        JLabel courseText = new JLabel();
        JLabel bestStudentText = new JLabel();

        courseText.setBounds(0,0,100,100);
        courseText.setLocation(0,0);
        bestStudentText.setBounds(500,300, 200,50);


        courseListModel = new DefaultListModel<>();
        courseJList = new JList<>(courseListModel);
        courseJList.setFixedCellWidth(300);
        courseJList.setFixedCellHeight(100);
        for (Course course:Catalog.getInstance().getCourses()) {
            if(user instanceof Teacher) {
                if (course.getTeacher().equals(user))
                    courseListModel.addElement(course);
            }
            if(user instanceof Assistant)
            {
                for (Assistant assistant: course.assistants) {
                    if(assistant.equals(user))
                        courseListModel.addElement(course);
                }
            }
        }
        listPanel.setBounds(0,100,300,400);
        listPanel.add(courseJList);

        gradesPanel.setBounds(400,100,400,160);
        gradesText = new ArrayList<>();
        gradesPanel.setLayout(new BoxLayout(gradesPanel, BoxLayout.Y_AXIS));
        gradesPanel.setBackground(new Color(200,200,200));

        button = new JButton();
        button.setBounds(500,0, 200,100);
        button.setText("Update Grades");

        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (user instanceof Teacher) {
                    for (Course course : Catalog.getInstance().getCourses()) {
                        if (course.getTeacher().equals(user))
                            course.getTeacher().accept(JsonParser.getInstance().scoreVisitor);
                    }
                }
                if (user instanceof Assistant) {
                    for (Course course : Catalog.getInstance().getCourses()) {
                        for (Assistant assistant : course.assistants) {
                            if (assistant.equals(user))
                                assistant.accept(JsonParser.getInstance().scoreVisitor);
                        }
                    }
                }
                if(gradesText.isEmpty()) {
                    for (int i = 0; i < courseJList.getSelectedValue().grades.size(); i++) {
                        gradesText.add(new JLabel());
                    }
                }
                for (JLabel label: gradesText) {
                    gradesPanel.add(label);
                    if(user instanceof Assistant) {
                        Group assistantGroup = null;
                        for (Group group: courseJList.getSelectedValue().groupDictionary.values()) {
                            if(group.getAssistant().equals(user))
                                assistantGroup = group;
                        }

                        Student student = assistantGroup.get(gradesText.indexOf(label));
                        Grade grade = courseJList.getSelectedValue().getGrade(student);

                        label.setText(grade.toString() + " Total: " + grade.getTotal());
                    }
                    if(user instanceof Teacher) {

                        Grade grade = courseJList.getSelectedValue().grades.get(gradesText.indexOf(label));
                        label.setText(grade.toString() + " Total: " + grade.getTotal());
                        bestStudentText.setText("Best Student is: " + courseJList.getSelectedValue().getBestStudent().toString());
                    }
                    System.out.println(courseJList.getSelectedValue().getGraduatedStudents());
                    //TODO GRADUATED STUDENTS
                }
            }
        };
        setLayout(null);
        add(bestStudentText);
        add(button);
        add(listPanel);
        add(userPanel);
        add(gradesPanel);
        setVisible(true);
    }
    public static void main(String[] args) throws IOException, ParseException {

        JsonParser.getInstance().parse();
        Teacher_AssistantPage teacherPage = new Teacher_AssistantPage("Teacher Page",new Teacher("Ion","Mihalache"));
        Teacher_AssistantPage assistantPage1 = new Teacher_AssistantPage("Assistant Page",new Assistant("Alexandra","Maria"));
        Teacher_AssistantPage assistantPage2 = new Teacher_AssistantPage("Assistant Page",new Assistant("Andrei","Georgescu"));
    }
}
