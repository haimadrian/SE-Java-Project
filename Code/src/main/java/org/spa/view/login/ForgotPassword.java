package org.spa.view.login;

import org.spa.controller.SPAApplication;
import org.spa.controller.user.User;
import org.spa.model.user.Customer;
import org.spa.view.util.Controls;
import org.spa.view.util.ImagesCache;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static org.spa.driver.SPAMain.FRAME_ICON_NAME;

public class ForgotPassword {

   private final Window owner;
   private JFrame frame;

   public ForgotPassword(Window owner) {
      this.owner = owner;
      frame = new JFrame("Forgot Password");
      frame.setIconImage(ImagesCache.getInstance().getImage(FRAME_ICON_NAME).getImage());
      frame.setSize(300, 200);
      frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      Controls.centerDialog(owner, frame);
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

      ActionListener okActionListener = new ActionListener() {

         @Override
         public void actionPerformed(ActionEvent e) {
            String userQuestion = SPAApplication.getInstance().getUserManagementService().forgotPasswordDisplayQuestion(userText.getText());
            if (userQuestion.equals("")) {
               res.setText("Wrong UserId!");
            } else {
               question.setText(userQuestion);
               userText.setVisible(false);
               userLabel.setVisible(false);
               OK.setVisible(false);
               submit.setVisible(true);
               answer.setVisible(true);
               res.setText("");
            }
         }
      };

      OK.addActionListener(okActionListener);
      userText.addActionListener(okActionListener);

      submit.addActionListener(new ActionListener() {

         @Override
         public void actionPerformed(ActionEvent e) {
            if(SPAApplication.getInstance().getUserManagementService().forgotPasswordCheckAnswer(userText.getText(), answer.getText())){
               frame.dispose();
               frame = null;
               new ResetPassword(owner, userText.getText());
            }
             else {
               res.setBounds(60, 5, 230, 20);
               res.setText("Wrong answer, try again!");
               answer.setText("");
            }
         }
      });
   }
}
