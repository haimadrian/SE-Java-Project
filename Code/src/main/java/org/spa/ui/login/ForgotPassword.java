package org.spa.ui.login;

import org.spa.common.SPAApplication;
import org.spa.common.User;
import org.spa.model.user.Customer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ForgotPassword {

   private final Window owner;
   private JFrame frame;

   public ForgotPassword(Window owner) {
      this.owner = owner;
      frame = new JFrame("Forgot Password");
      frame.setSize(300, 200);
      frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      frame.setLocationRelativeTo(null);
      JPanel panel = new JPanel();
      frame.add(panel);
      placeComponents(panel);

      frame.setVisible(true);
   }

   private void placeComponents(JPanel panel) {

      panel.setLayout(null);

      JLabel res = new JLabel("");
      res.setBounds(100, 7, 200, 25);
      panel.add(res);

      JLabel question = new JLabel("");
      question.setBounds(60, 30, 230, 25);
      panel.add(question);

      JLabel userLabel = new JLabel("User Id");
      userLabel.setBounds(30, 30, 60, 25);
      panel.add(userLabel);

      JTextField userText = new JTextField(20);
      userText.setBounds(100, 30, 150, 25);
      panel.add(userText);

      JButton OK = new JButton("Ok");
      OK.setBounds(100, 80, 85, 25);
      panel.add(OK);

      JButton submit = new JButton("Submit");
      submit.setBounds(90, 130, 85, 25);
      panel.add(submit);
      submit.setVisible(false);

      JTextArea answer = new JTextArea();
      answer.setBounds(60, 70, 150, 50);
      panel.add(answer);
      answer.setVisible(false);

      OK.addActionListener(new ActionListener() {

         @Override
         public void actionPerformed(ActionEvent e) {
            User u = SPAApplication.getInstance().getUserManagementService().getUser(userText.getText());
            String userQuestion = "";
            if (u == null) {
               res.setText("Wrong UserId!");
            } else if (u instanceof Customer) {
               userQuestion = ((Customer) u).getSecretQuestion();
               question.setText(userQuestion);
               userText.setVisible(false);
               userLabel.setVisible(false);
               OK.setVisible(false);
               submit.setVisible(true);
               answer.setVisible(true);
               res.setText("");
            }
         }
      });

      submit.addActionListener(new ActionListener() {

         @Override
         public void actionPerformed(ActionEvent e) {
            User u = SPAApplication.getInstance().getUserManagementService().getUser(userText.getText());
            String userAnswer = "";
            if (u instanceof Customer) {
               userAnswer = ((Customer) u).getSecretAnswer();
            }
            if (userAnswer.equals(answer.getText())) {
               frame.dispose();
               frame = null;
               new ResetPassword(owner, u);
            } else {
               res.setBounds(60, 8, 230, 25);
               res.setText("Wrong answer, try again!");
               answer.setText("");
            }
         }
      });
   }
}
