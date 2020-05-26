package org.spa.ui;
import org.spa.common.User;
import org.spa.common.SPAApplication;
import org.spa.model.user.Customer;
import org.spa.model.user.SystemAdmin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import static javax.swing.JOptionPane.showMessageDialog;

public class ResetPassword {

    private JFrame frame;
    private User currentUser;

    public void ResetPassword() {
        frame = new JFrame("Reset Password");
        frame.setSize(350, 200);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
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

        reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String newPass = new String(newPasswordText.getPassword());
                String reEnterPass = new String(reEnterPasswordText.getPassword());

                if (newPass.length() == 0 || reEnterPass.length() == 0)
                    res.setText("Fill the empty fields!");
                else if (newPass.equals(reEnterPass)) {
                    showMessageDialog(null, "Password changed successfully!");
                    SPAApplication.getInstance().getUserManagementService().updateUser(currentUser, newPass);
                    frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                } else {
                    res.setText("Password do not match!");
                }
            }
        });
    }

    public void currentUser(User user) {
        this.currentUser = user;
    }
}
