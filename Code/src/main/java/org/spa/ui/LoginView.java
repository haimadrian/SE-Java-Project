package org.spa.ui;

import org.spa.common.SPAApplication;
import org.spa.common.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginView {

    private JDialog frame;
    private final Window owner;

    public LoginView(Window owner) {
        this.owner = owner;
        frame = new JDialog(owner, "Login", Dialog.ModalityType.APPLICATION_MODAL);
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

        JLabel res = new JLabel("");
        res.setBounds(120, 7, 200, 25);
        panel.add(res);

        JLabel userLabel = new JLabel("User");
        userLabel.setBounds(30, 30, 80, 25);
        panel.add(userLabel);

        JTextField userText = new JTextField(20);
        userText.setBounds(120, 30, 175, 25);
        panel.add(userText);

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setBounds(30, 60, 80, 25);
        panel.add(passwordLabel);

        JPasswordField passwordText = new JPasswordField(20);
        passwordText.setBounds(120, 60, 175, 25);
        panel.add(passwordText);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(30, 100, 85, 25);
        panel.add(loginButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setBounds(120, 100, 85, 25);
        panel.add(cancelButton);

        JButton registerButton = new JButton("Register");
        registerButton.setBounds(210, 100, 85, 25);
        panel.add(registerButton);

        JLabel forgotPassword = new JLabel("Forgot Password?" );
        forgotPassword.setBounds(115, 130, 130, 25);
        forgotPassword.setForeground(Color.BLUE);
        panel.add(forgotPassword);


        loginButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                User loggedInUser = SPAApplication.getInstance().getUserManagementService().login(userText.getText(),new String(passwordText.getPassword()));
                if(loggedInUser != null) {
                    // close login form
                    frame.dispose();
                    frame = null;
                }
                else {
                    res.setText("Invalid Username or password!");
                    userText.setText("");;
                    passwordText.setText("");;
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                //your actions
                frame.dispose();
                frame = null;
            }
        });

        registerButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                frame = null;
                new Registration(owner);
            }
        });

        forgotPassword.addMouseListener(new MouseAdapter()
        {
            public void mouseClicked(MouseEvent e)
            {
                frame.dispose();
                frame = null;
                new ForgotPassword(owner);
            }
        });
    }
}
