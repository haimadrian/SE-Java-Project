package org.spa.view.login;

import org.spa.controller.SPAApplication;
import org.spa.controller.user.UserType;
import org.spa.view.util.Controls;
import org.spa.view.util.ImagesCache;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Date;

import static javax.swing.JOptionPane.showMessageDialog;
import static org.spa.driver.SPAMain.FRAME_ICON_NAME;

public class Registration
      extends JFrame
      implements ActionListener {

   private final Window owner;
   // Components of the Form
   private final Container c;
   private final JLabel title;
   private final JLabel name;
   private final JTextField tname;
   private final JLabel password;
   private final JPasswordField tpassword;
   private final JLabel mno;
   private final JTextField tmno;
   private final JLabel gender;
   private final JRadioButton male;
   private final JRadioButton female;
   private final ButtonGroup gengp;
   private final JLabel question;
   private final JComboBox tquestion;
   private final JLabel dob;
   private final JComboBox day;
   private final JComboBox month;
   private final JComboBox year;
   private final JLabel answer;
   private final JTextArea tanswer;
   private final JCheckBox term;
   private final JButton sub;
   private final JButton reset;
   private final JLabel res;
   private final String[] days
         = { "1", "2", "3", "4", "5",
         "6", "7", "8", "9", "10",
         "11", "12", "13", "14", "15",
         "16", "17", "18", "19", "20",
         "21", "22", "23", "24", "25",
         "26", "27", "28", "29", "30",
         "31" };
   private final String[] months
         = { "01", "02", "03", "04",
         "05", "06", "07", "08",
         "09", "10", "11", "12" };
   private final String[] years
         = { "1966", "1967", "1968", "1969",
         "1970", "1971", "1972", "1973",
         "1974", "1975", "1976", "1977",
         "1978", "1979", "1980", "1981",
         "1982", "1983", "1984", "1985",
         "1986", "1987", "1988", "1989",
         "1990", "1991", "1992", "1993",
         "1994", "1995", "1996", "1997",
         "1998", "1999", "2000", "2001",
         "2002", "2003", "2004", "2005" };
   private final String[] questions
         = { "What is your favourite color ?",
         "What is your Mom's old last name ?",
         "What is the name of your best friend ?",
         "What is your preffered hobbie ?" };
   private final String[] userTypes
         = { "System Admin",
         "Admin",
         "Customer" };
   private JLabel usertype;
   private JComboBox tusertype;

   // constructor, to initialize the components
   // with default values.
   public Registration(Window owner) {
      this.owner = owner;
      setTitle("Registration");
      setSize(450, 610);
      setDefaultCloseOperation(DISPOSE_ON_CLOSE);
      setResizable(false);
      setIconImage(ImagesCache.getInstance().getImage(FRAME_ICON_NAME).getImage());
      Controls.centerDialog(this);
      c = getContentPane();
      c.setLayout(null);

      title = new JLabel("Registration");
      title.setFont(new Font("Arial", Font.PLAIN, 30));
      title.setSize(300, 45);
      title.setLocation(150, 20);
      c.add(title);

      name = new JLabel("Username");
      name.setFont(new Font("Arial", Font.PLAIN, 20));
      name.setSize(100, 30);
      name.setLocation(45, 100);
      c.add(name);

      tname = new JTextField();
      tname.setFont(new Font("Arial", Font.PLAIN, 15));
      tname.setSize(260, 30);
      tname.setLocation(145, 100);
      c.add(tname);

      password = new JLabel("Password");
      password.setFont(new Font("Arial", Font.PLAIN, 20));
      password.setSize(100, 30);
      password.setLocation(45, 150);
      c.add(password);

      tpassword = new JPasswordField();
      tpassword.setFont(new Font("Arial", Font.PLAIN, 15));
      tpassword.setSize(260, 30);
      tpassword.setLocation(145, 150);
      c.add(tpassword);

      mno = new JLabel("Mobile");
      mno.setFont(new Font("Arial", Font.PLAIN, 20));
      mno.setSize(100, 30);
      mno.setLocation(45, 200);
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
      tmno.setSize(260, 30);
      tmno.setLocation(145, 200);
      c.add(tmno);

      gender = new JLabel("Gender");
      gender.setFont(new Font("Arial", Font.PLAIN, 20));
      gender.setSize(100, 30);
      gender.setLocation(45, 250);
      c.add(gender);

      male = new JRadioButton("Male");
      male.setFont(new Font("Arial", Font.PLAIN, 15));
      male.setSelected(true);
      male.setSize(75, 30);
      male.setLocation(145, 250);
      c.add(male);

      female = new JRadioButton("Female");
      female.setFont(new Font("Arial", Font.PLAIN, 15));
      female.setSelected(false);
      female.setSize(80, 30);
      female.setLocation(220, 250);
      c.add(female);

      gengp = new ButtonGroup();
      gengp.add(male);
      gengp.add(female);

      dob = new JLabel("Date Of Birth");
      dob.setFont(new Font("Arial", Font.PLAIN, 20));
      dob.setSize(130, 30);
      dob.setLocation(45, 300);
      c.add(dob);

      day = new JComboBox(days);
      day.setFont(new Font("Arial", Font.PLAIN, 15));
      day.setSize(50, 30);
      day.setLocation(175, 300);
      c.add(day);

      month = new JComboBox(months);
      month.setFont(new Font("Arial", Font.PLAIN, 15));
      month.setSize(60, 30);
      month.setLocation(230, 300);
      c.add(month);

      year = new JComboBox(years);
      year.setFont(new Font("Arial", Font.PLAIN, 15));
      year.setSize(70, 30);
      year.setLocation(295, 300);
      c.add(year);

      question = new JLabel("Question");
      question.setFont(new Font("Arial", Font.PLAIN, 20));
      question.setSize(100, 30);
      question.setLocation(45, 350);
      c.add(question);

      tquestion = new JComboBox(questions);
      tquestion.setFont(new Font("Arial", Font.PLAIN, 15));
      tquestion.setSize(260, 30);
      tquestion.setLocation(145, 350);
      c.add(tquestion);

      answer = new JLabel("Answer");
      answer.setFont(new Font("Arial", Font.PLAIN, 20));
      answer.setSize(100, 30);
      answer.setLocation(45, 400);
      c.add(answer);

      tanswer = new JTextArea();
      tanswer.setFont(new Font("Arial", Font.PLAIN, 15));
      tanswer.setSize(260, 60);
      tanswer.setLocation(145, 400);
      tanswer.setLineWrap(true);
      c.add(tanswer);

      if (SPAApplication.getInstance().getUserManagementService().getLoggedInUserType() == (UserType.SysAdmin)) {
         usertype = new JLabel("User Type");
         usertype.setFont(new Font("Arial", Font.PLAIN, 20));
         usertype.setSize(100, 30);
         usertype.setLocation(45, 475);
         c.add(usertype);

         tusertype = new JComboBox(userTypes);
         tusertype.setFont(new Font("Arial", Font.PLAIN, 15));
         tusertype.setSize(260, 30);
         tusertype.setLocation(145, 475);
         c.add(tusertype);
      }

      if (Controls.isDarkMode()) {
         day.setBackground(Color.black);
         day.setForeground(Color.white);
         month.setBackground(Color.black);
         month.setForeground(Color.white);
         year.setBackground(Color.black);
         year.setForeground(Color.white);
         tquestion.setBackground(Color.black);
         tquestion.setForeground(Color.white);

         if (tusertype != null) {
            tusertype.setBackground(Color.black);
            tusertype.setForeground(Color.white);
         }
      }

      term = new JCheckBox("Accept Terms And Conditions.");
      term.setFont(new Font("Arial", Font.PLAIN, 15));
      term.setSize(260, 20);
      term.setLocation(120, 505);
      c.add(term);

      sub = new JButton("Submit");
      sub.setFont(new Font("Arial", Font.PLAIN, 15));
      sub.setSize(100, 30);
      sub.setLocation(130, 535);
      sub.addActionListener(this);
      c.add(sub);

      reset = new JButton("Reset");
      reset.setFont(new Font("Arial", Font.PLAIN, 15));
      reset.setSize(100, 30);
      reset.setLocation(245, 535);
      reset.addActionListener(this);
      c.add(reset);

      res = new JLabel("");
      res.setFont(new Font("Arial", Font.PLAIN, 20));
      res.setSize(500, 30);
      res.setLocation(50, 65);
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
         String userType = (SPAApplication.getInstance().getUserManagementService().getLoggedInUserType() == UserType.Guest ? null : (String) tusertype.getSelectedItem());
         if (term.isSelected()) {
            if (!SPAApplication.getInstance().getUserManagementService().
                  register(tname.getText(), new String(tpassword.getPassword()),
                        tmno.getText(), new Date(comboBoxValueToInt(year) + "/" + comboBoxValueToInt(month) + "/" + comboBoxValueToInt(day)),
                        new Date(System.currentTimeMillis()), (String) tquestion.getSelectedItem(),
                        (String) tanswer.getText(), SPAApplication.getInstance().getUserManagementService().getLoggedInUserType(), userType)) {
               if (tname.getText().isEmpty() || tpassword.getPassword().length == 0
                     || tanswer.getText().isEmpty() || tmno.getText().isEmpty()) {
                  res.setText("Please fill the empty fields");
               } else {
                  res.setText("Username Already Exist");
               }
            } else {
               showMessageDialog(null, "Registration Completed Successfully");
               dispose();
               if(SPAApplication.getInstance().getUserManagementService().getLoggedInUserType() != UserType.SysAdmin) {
                  new LoginView(owner);
               }
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

         if (tusertype != null) {
            tusertype.setSelectedIndex(0);
         }
      }
   }

}
