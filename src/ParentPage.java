import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ParentPage extends JFrame {
    JList<Notification> notificationJList;
    DefaultListModel<Notification> notifDefaultListModel;
    JButton updateButton;

    public JLabel alertText;
    public ParentPage(String name, Parent parent){
        super(name);
        setMinimumSize(new Dimension(800,800));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel notifPanel = new JPanel();
        JLabel parentText;

        parentText = new JLabel();
        parentText.setText("Parent: " + parent);
        parentText.setBounds(50,50,150,50);

        alertText = new JLabel();
        alertText.setBounds(50, 100, 150,50);
        updateButton = new JButton();

        updateButton.setText("View notifications");
        updateButton.setBounds(50,200,150,50);


        notifDefaultListModel = new DefaultListModel<>();
        notificationJList = new JList<>(notifDefaultListModel);
        notifPanel.add(notificationJList);
        notifPanel.setBounds(250,0, 550,300);

        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                for (Observer observer : Catalog.getInstance().observers) {
                    if(((Parent)observer).equals(parent) && !notifDefaultListModel.contains(((Parent)observer).notification)){
                        updateButton.setEnabled(true);
                        notifDefaultListModel.addElement(((Parent)observer).notification);
                    }
                }
            }
        };
        updateButton.addActionListener(actionListener);

        setLayout(null);
        add(parentText);
        add(updateButton);
        add(notifPanel);

        setVisible(true);
    }
    public static void main(String[] args) throws IOException, ParseException {
        JsonParser.getInstance().parse();

        Teacher_AssistantPage assistantPage2 = new Teacher_AssistantPage("Assistant Page",new Assistant("Andrei","Georgescu"));

        ParentPage parentPage = new ParentPage("Parent Page", new Parent("Daniel","Frone"));

    }
}
