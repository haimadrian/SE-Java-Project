package org.spa.view.login;

import org.spa.controller.SPAApplication;
import org.spa.view.util.Controls;
import org.spa.view.util.ImagesCache;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static javax.swing.JOptionPane.showMessageDialog;
import static org.spa.driver.SPAMain.FRAME_ICON_NAME;

public class ResetPassword {

   private final Window owner;
   private final String currentUserId;
   private JFrame frame;

   public ResetPassword(Window owner, String userId) {
      this.owner = owner;
      currentUserId = userId;
      frame = new JFrame("Reset Password");
      frame.setIconImage(ImagesCache.getInstance().getImage(FRAME_ICON_NAME).getImage());
      frame.setSize(350, 200);
      frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      Controls.centerDialog(owner, frame);
      JPanel panel = new JPanel();
      frame.add(panel);
      placeComponents(panel);

      frame.setVisible(true);
   }

   private void placeComponents(JPanel panel) {

      panel.setLayout(null);

      JLabel res = new JLabel("Enter a new Password");
      res.setBounds(150, 7, 200, 25);
      panel.add(res);

      JLabel newPassword = new JLabel("New Password");
      newPassword.setBounds(30, 30, 110, 25);
      panel.add(newPassword);

      JPasswordField newPasswordText = new JPasswordField(20);
      newPasswordText.setBounds(150, 30, 150, 25);
      panel.add(newPasswordText);

      JLabel reEnterPassword = new JLabel("Re-enter Password");
      reEnterPassword.setBounds(30, 60, 110, 25);
      panel.add(reEnterPassword);

      JPasswordField reEnterPasswordText = new JPasswordField(20);
      reEnterPasswordText.setBounds(150, 60, 150, 25);
      panel.add(reEnterPasswordText);

      JButton reset = new JButton("Reset Password");
      reset.setBounds(30, 100, 120, 25);
      panel.add(reset);

      ActionListener resetActionListener = new ActionListener() {

         @Override
         public void actionPerformed(ActionEvent e) {
            if (SPAApplication.getInstance().getUserManagementService().resetPassword(new String(newPasswordText.getPassword()), new String(reEnterPasswordText.getPassword()), currentUserId)) {
               showMessageDialog(null, "Password changed successfully!");
               frame.dispose();
               frame = null;
               new LoginView(owner);
            } else if (newPasswordText.getPassword().length == 0 || reEnterPassword.getText().length() == 0) {
               res.setText("Fill the empty fields!");
            } else {
               res.setText("Password do not match!");
            }
         }
      };

      reset.addActionListener(resetActionListener);
      newPasswordText.addActionListener(resetActionListener);
      reEnterPasswordText.addActionListener(resetActionListener);
   }
}
