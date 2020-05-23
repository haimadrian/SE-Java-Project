package ui;

import controller.UserManagementService;
import model.Customer;
import model.Manager;
import model.SystemAdmin;
import org.spa.common.SPAApplication;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class Registration
        extends JFrame
        implements ActionListener {

    // Components of the Form
    private Container c;
    private JLabel title;
    private JLabel name;
    private JTextField tname;
    private JLabel password;
    private JPasswordField tpassword;
    private JLabel mno;
    private JTextField tmno;
    private JLabel gender;
    private JRadioButton male;
    private JRadioButton female;
    private ButtonGroup gengp;
    private JLabel question;
    private JComboBox tquestion;
    private JLabel dob;
    private JComboBox date;
    private JComboBox month;
    private JComboBox year;
    private JLabel answer;
    private JTextArea tanswer;
    private JLabel usertype;
    private JComboBox tusertype;
    private JCheckBox term;
    private JButton sub;
    private JButton reset;
    private JTextArea tout;
    private JLabel res;
    private JTextArea resadd;

    private String dates[]
            = { "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "10",
            "11", "12", "13", "14", "15",
            "16", "17", "18", "19", "20",
            "21", "22", "23", "24", "25",
            "26", "27", "28", "29", "30",
            "31" };
    private String months[]
            = { "Jan", "feb", "Mar", "Apr",
            "May", "Jun", "July", "Aug",
            "Sup", "Oct", "Nov", "Dec" };
    private String years[]
            = { "1995", "1996", "1997", "1998",
            "1999", "2000", "2001", "2002",
            "2003", "2004", "2005", "2006",
            "2007", "2008", "2009", "2010",
            "2011", "2012", "2013", "2014",
            "2015", "2016", "2017", "2018",
            "2019" };

    private String questions[]
            = {"What is your favourite color ?",
            "What is your Mom's old last name ?",
            "What is the name of your best friend ?" ,
            "What is your preffered hobbie ?"};

    private String userTypes[]
            = {"System Admin",
            "Manager",
            "Customer"};

    // constructor, to initialize the components
    // with default values.
    public Registration()
    {
        setTitle("Registration Form");
        setBounds(300, 90, 600, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        c = getContentPane();
        c.setLayout(null);

        title = new JLabel("Registration Form");
        title.setFont(new Font("Arial", Font.PLAIN, 30));
        title.setSize(300, 30);
        title.setLocation(170, 30);
        c.add(title);

        name = new JLabel("Username");
        name.setFont(new Font("Arial", Font.PLAIN, 20));
        name.setSize(100, 20);
        name.setLocation(120, 100);
        c.add(name);

        tname = new JTextField();
        tname.setFont(new Font("Arial", Font.PLAIN, 15));
        tname.setSize(190, 20);
        tname.setLocation(220, 100);
        c.add(tname);

        password = new JLabel("Password");
        password.setFont(new Font("Arial", Font.PLAIN, 20));
        password.setSize(100, 20);
        password.setLocation(120, 150);
        c.add(password);

        tpassword = new JPasswordField();
        tpassword.setFont(new Font("Arial", Font.PLAIN, 15));
        tpassword.setSize(190, 20);
        tpassword.setLocation(220, 150);
        c.add(tpassword);

        mno = new JLabel("Mobile");
        mno.setFont(new Font("Arial", Font.PLAIN, 20));
        mno.setSize(100, 20);
        mno.setLocation(120, 200);
        c.add(mno);

        tmno = new JTextField();
        tmno.setFont(new Font("Arial", Font.PLAIN, 15));
        tmno.setSize(150, 20);
        tmno.setLocation(220, 200);
        c.add(tmno);

        gender = new JLabel("Gender");
        gender.setFont(new Font("Arial", Font.PLAIN, 20));
        gender.setSize(100, 20);
        gender.setLocation(120, 250);
        c.add(gender);

        male = new JRadioButton("Male");
        male.setFont(new Font("Arial", Font.PLAIN, 15));
        male.setSelected(true);
        male.setSize(75, 20);
        male.setLocation(220, 250);
        c.add(male);

        female = new JRadioButton("Female");
        female.setFont(new Font("Arial", Font.PLAIN, 15));
        female.setSelected(false);
        female.setSize(80, 20);
        female.setLocation(295, 250);
        c.add(female);

        gengp = new ButtonGroup();
        gengp.add(male);
        gengp.add(female);

        dob = new JLabel("DOB");
        dob.setFont(new Font("Arial", Font.PLAIN, 20));
        dob.setSize(100, 20);
        dob.setLocation(120, 300);
        c.add(dob);

        date = new JComboBox(dates);
        date.setFont(new Font("Arial", Font.PLAIN, 15));
        date.setSize(50, 20);
        date.setLocation(220, 300);
        c.add(date);

        month = new JComboBox(months);
        month.setFont(new Font("Arial", Font.PLAIN, 15));
        month.setSize(60, 20);
        month.setLocation(270, 300);
        c.add(month);

        year = new JComboBox(years);
        year.setFont(new Font("Arial", Font.PLAIN, 15));
        year.setSize(60, 20);
        year.setLocation(340, 300);
        c.add(year);

        question = new JLabel("Question");
        question.setFont(new Font("Arial", Font.PLAIN, 20));
        question.setSize(100, 20);
        question.setLocation(120, 350);
        c.add(question);

        tquestion = new JComboBox(questions);
        tquestion.setFont(new Font("Arial", Font.PLAIN, 15));
        tquestion.setSize(250, 20);
        tquestion.setLocation(220, 350);
        c.add(tquestion);

        answer = new JLabel("Answer");
        answer.setFont(new Font("Arial", Font.PLAIN, 20));
        answer.setSize(100, 20);
        answer.setLocation(120, 400);
        c.add(answer);

        tanswer = new JTextArea();
        tanswer.setFont(new Font("Arial", Font.PLAIN, 15));
        tanswer.setSize(250, 60);
        tanswer.setLocation(220, 400);
        tanswer.setLineWrap(true);
        c.add(tanswer);

        usertype = new JLabel("User Type");
        usertype.setFont(new Font("Arial", Font.PLAIN, 20));
        usertype.setSize(100, 20);
        usertype.setLocation(120, 475);
        c.add(usertype);

        tusertype = new JComboBox(userTypes);
        tusertype.setFont(new Font("Arial", Font.PLAIN, 15));
        tusertype.setSize(250, 20);
        tusertype.setLocation(220, 475);
        c.add(tusertype);

        term = new JCheckBox("Accept Terms And Conditions.");
        term.setFont(new Font("Arial", Font.PLAIN, 15));
        term.setSize(250, 20);
        term.setLocation(205, 510);
        c.add(term);

        sub = new JButton("Submit");
        sub.setFont(new Font("Arial", Font.PLAIN, 15));
        sub.setSize(100, 20);
        sub.setLocation(220, 540);
        sub.addActionListener(this);
        c.add(sub);

        reset = new JButton("Reset");
        reset.setFont(new Font("Arial", Font.PLAIN, 15));
        reset.setSize(100, 20);
        reset.setLocation(335, 540);
        reset.addActionListener(this);
        c.add(reset);

//        tout = new JTextArea();
//        tout.setFont(new Font("Arial", Font.PLAIN, 15));
//        tout.setSize(300, 400);
//        tout.setLocation(500, 100);
//        tout.setLineWrap(true);
//        tout.setEditable(false);
//        c.add(tout);

//        res = new JLabel("");
//        res.setFont(new Font("Arial", Font.PLAIN, 20));
//        res.setSize(500, 25);
//        res.setLocation(100, 500);
//        c.add(res);
//
//        resadd = new JTextArea();
//        resadd.setFont(new Font("Arial", Font.PLAIN, 15));
//        resadd.setSize(200, 75);
//        resadd.setLocation(580, 175);
//        resadd.setLineWrap(true);
//        c.add(resadd);

        setVisible(true);
    }

    // method actionPerformed()
    // to get the action performed
    // by the user and act accordingly
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == sub) {
            if (term.isSelected()) {
              String data = (String)tusertype.getSelectedItem();
                switch(data) {
                    case "System Admin":
                        SystemAdmin sa = new SystemAdmin("1234");
                        SPAApplication.getInstance().getUserManagementService().createUser(sa);
                        break;
                    case "Manager":
                        Manager manager = new Manager(new String(tpassword.getPassword()), tmno.getText(), (String) date.getSelectedItem()
                                + "/" + (String) month.getSelectedItem()
                                + "/" + (String) year.getSelectedItem(), "1/1/2020", (String) tquestion.getSelectedItem(), (String)tanswer.getText(), 0, 0);
                        SPAApplication.getInstance().getUserManagementService().createUser(manager);
                        break;
                    case "Customer":
                        Customer customer = new Customer(new String(tpassword.getPassword()), tmno.getText(), (String) date.getSelectedItem()
                                + "/" + (String) month.getSelectedItem()
                                + "/" + (String) year.getSelectedItem(), "1/1/2020", (String) tquestion.getSelectedItem(), (String)tanswer.getText());
                        SPAApplication.getInstance().getUserManagementService().createUser(customer);
                        break;
                }

                        // code block
//                String data1;
//                String data
//                        = "Name : "
//                        + tname.getText() + "\n"
//                        + "Mobile : "
//                        + tmno.getText() + "\n";
//                if (male.isSelected())
//                    data1 = "Gender : Male"
//                            + "\n";
//                else
//                    data1 = "Gender : Female"
//                            + "\n";
//                String data2
//                        = "DOB : "
//                        + (String)date.getSelectedItem()
//                        + "/" + (String)month.getSelectedItem()
//                        + "/" + (String)year.getSelectedItem()
//                        + "\n";
//
//                String data3 = "Address : " + tanswer.getText();
//                tout.setText(data + data1 + data2 + data3);
//                tout.setEditable(false);
//                res.setText("Registration Successfully..");
            }
            else {
                tout.setText("");
                resadd.setText("");
                res.setText("Please accept the"
                        + " terms & conditions..");
            }
        }

        else if (e.getSource() == reset) {
            String def = "";
            tname.setText(def);
            tanswer.setText(def);
            tpassword.setText(def);
            tmno.setText(def);
            term.setSelected(false);
            date.setSelectedIndex(0);
            month.setSelectedIndex(0);
            year.setSelectedIndex(0);
            tquestion.setSelectedIndex(0);
            tusertype.setSelectedIndex(0);
        }
    }
}
