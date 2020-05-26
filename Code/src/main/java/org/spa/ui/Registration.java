package org.spa.ui;

import org.spa.common.User;
import org.spa.controller.UserManagementService;
import org.spa.controller.UserType;
import org.spa.model.user.Customer;
import org.spa.model.user.Admin;
import org.spa.model.user.SystemAdmin;
import org.spa.common.SPAApplication;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Date;

import static javax.swing.JOptionPane.showMessageDialog;

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
    private JComboBox day;
    private JComboBox month;
    private JComboBox year;
    private JLabel answer;
    private JTextArea tanswer;
    private JLabel usertype;
    private JComboBox tusertype;
    private JCheckBox term;
    private JButton sub;
    private JButton reset;
    private JLabel res;

    private String days[]
            = {"1", "2", "3", "4", "5",
            "6", "7", "8", "9", "10",
            "11", "12", "13", "14", "15",
            "16", "17", "18", "19", "20",
            "21", "22", "23", "24", "25",
            "26", "27", "28", "29", "30",
            "31"};

    private String months[]
            = {"01", "02", "03", "04",
            "05", "06", "07", "08",
            "09", "10", "11", "12"};

    private String years[]
            = {"1966", "1967", "1968", "1969",
            "1970", "1971", "1972", "1973",
            "1974", "1975", "1976", "1977",
            "1978", "1979", "1980", "1981",
            "1982", "1983", "1984", "1985",
            "1986", "1987", "1988", "1989",
            "1990", "1991", "1992", "1993",
            "1994", "1995", "1996", "1997",
            "1998", "1999", "2000", "2001",
            "2002", "2003", "2004", "2005"};

    private String questions[]
            = {"What is your favourite color ?",
            "What is your Mom's old last name ?",
            "What is the name of your best friend ?",
            "What is your preffered hobbie ?"};

    private String userTypes[]
            = {"System Admin",
            "Manager",
            "Customer"};

    // constructor, to initialize the components
    // with default values.
    public Registration() {
        setTitle("Registration");
        setBounds(600, 200, 600, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);

        c = getContentPane();
        c.setLayout(null);

        title = new JLabel("Registration");
        title.setFont(new Font("Arial", Font.PLAIN, 30));
        title.setSize(300, 30);
        title.setLocation(220, 25);
        c.add(title);

        name = new JLabel("Username");
        name.setFont(new Font("Arial", Font.PLAIN, 20));
        name.setSize(100, 20);
        name.setLocation(120, 100);
        c.add(name);

        tname = new JTextField();
        tname.setFont(new Font("Arial", Font.PLAIN, 15));
        tname.setSize(190, 25);
        tname.setLocation(220, 100);
        c.add(tname);

        password = new JLabel("Password");
        password.setFont(new Font("Arial", Font.PLAIN, 20));
        password.setSize(100, 20);
        password.setLocation(120, 150);
        c.add(password);

        tpassword = new JPasswordField();
        tpassword.setFont(new Font("Arial", Font.PLAIN, 15));
        tpassword.setSize(190, 25);
        tpassword.setLocation(220, 150);
        c.add(tpassword);

        mno = new JLabel("Mobile");
        mno.setFont(new Font("Arial", Font.PLAIN, 20));
        mno.setSize(100, 20);
        mno.setLocation(120, 200);
        c.add(mno);

        tmno = new JTextField() {
            public void processKeyEvent(KeyEvent ev) {
                char c = ev.getKeyChar();
                try {
                    // Ignore all non-printable characters. Just check the printable ones.
                    if (c > 31 && c < 127) {
                        Integer.parseInt(c + "");
                    }
                    super.processKeyEvent(ev);
                } catch (NumberFormatException nfe) {
                    // Do nothing. Character inputted is not a number, so ignore it.
                }
            }
        };
        tmno.setFont(new Font("Arial", Font.PLAIN, 15));
        tmno.setSize(150, 25);
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

        dob = new JLabel("Date Of Birth");
        dob.setFont(new Font("Arial", Font.PLAIN, 20));
        dob.setSize(130, 20);
        dob.setLocation(120, 300);
        c.add(dob);

        day = new JComboBox(days);
        day.setFont(new Font("Arial", Font.PLAIN, 15));
        day.setSize(50, 25);
        day.setLocation(250, 300);
        c.add(day);

        month = new JComboBox(months);
        month.setFont(new Font("Arial", Font.PLAIN, 15));
        month.setSize(60, 25);
        month.setLocation(305, 300);
        c.add(month);

        year = new JComboBox(years);
        year.setFont(new Font("Arial", Font.PLAIN, 15));
        year.setSize(70, 25);
        year.setLocation(370, 300);
        c.add(year);

        question = new JLabel("Question");
        question.setFont(new Font("Arial", Font.PLAIN, 20));
        question.setSize(100, 20);
        question.setLocation(120, 350);
        c.add(question);

        tquestion = new JComboBox(questions);
        tquestion.setFont(new Font("Arial", Font.PLAIN, 15));
        tquestion.setSize(250, 25);
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

        if (SPAApplication.getInstance().getUserManagementService().getLoggedInUserType() == (UserType.SysAdmin)) {
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
        }
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

        res = new JLabel("");
        res.setFont(new Font("Arial", Font.PLAIN, 20));
        res.setSize(500, 25);
        res.setLocation(120, 65);
        c.add(res);

        setVisible(true);
    }

    private static int comboBoxValueToInt(JComboBox<?> combo) {
        return Integer.parseInt(String.valueOf(combo.getSelectedItem()));
    }

    // method actionPerformed()
    // to get the action performed
    // by the user and act accordingly
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == sub) {
            if (term.isSelected()) {
                if (tname.getText().equals("") || tpassword.getPassword().length == 0
                        || tanswer.getText().equals("") || tmno.getText().equals("")) {
                    res.setText("Please fill the empty fields");
                }
                else if(SPAApplication.getInstance().getUserManagementService().isExist(tname.getText()))
                {
                    res.setText("Username Already Exist");
                }
                else if (SPAApplication.getInstance().getUserManagementService().getLoggedInUserType() == (UserType.Guest)) {
                    Customer customer = new Customer(tname.getText(), new String(tpassword.getPassword()), tmno.getText(),
                            new Date(comboBoxValueToInt(year) + "/" + comboBoxValueToInt(month) + "/" + comboBoxValueToInt(day)),
                            new Date(System.currentTimeMillis()), (String) tquestion.getSelectedItem(), (String) tanswer.getText());
                    SPAApplication.getInstance().getUserManagementService().createUser(customer);
                    showMessageDialog(null, "Registration Completed Successfully");
                    dispose();
                } else if (SPAApplication.getInstance().getUserManagementService().getLoggedInUserType() == (UserType.SysAdmin)) {
                    String data = (String) tusertype.getSelectedItem();
                    switch (data) {
                        case "System Admin":
                            SystemAdmin sa = new SystemAdmin(tname.getText(), new String(tpassword.getPassword()));
                            SPAApplication.getInstance().getUserManagementService().createUser(sa);
                            break;
                        case "Manager":
                            Admin admin = new Admin(tname.getText(), new String(tpassword.getPassword()), tmno.getText(),
                                    new Date(comboBoxValueToInt(year) + "/" + comboBoxValueToInt(month) + "/" + comboBoxValueToInt(day)),
                                    new Date(System.currentTimeMillis()), (String) tquestion.getSelectedItem(), (String) tanswer.getText(), 0, 0);
                            SPAApplication.getInstance().getUserManagementService().createUser(admin);
                            break;
                        case "Customer":
                            Customer customer = new Customer(tname.getText(), new String(tpassword.getPassword()), tmno.getText(),
                                    new Date(comboBoxValueToInt(year), comboBoxValueToInt(month), comboBoxValueToInt(day)),
                                    new Date(System.currentTimeMillis()), (String) tquestion.getSelectedItem(), (String) tanswer.getText());
                            SPAApplication.getInstance().getUserManagementService().createUser(customer);
                            break;
                    }
                    showMessageDialog(null, "Registration Completed Successfully");
                    dispose();
                }
            } else {
                res.setText("Please accept the terms");
            }

        } else if (e.getSource() == reset) {
            String def = "";
            tname.setText(def);
            tanswer.setText(def);
            tpassword.setText(def);
            tmno.setText(def);
            res.setText(def);
            term.setSelected(false);
            day.setSelectedIndex(0);
            month.setSelectedIndex(0);
            year.setSelectedIndex(0);
            tquestion.setSelectedIndex(0);
            tusertype.setSelectedIndex(0);
        }
    }

}