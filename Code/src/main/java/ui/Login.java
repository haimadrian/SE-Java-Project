package ui;


import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Login  {
    private JLabel lblLogin;
    private JTextField textField1;
    private JPasswordField passwordField1;
    private JButton okButton;
    private JButton cancelButton;
    private JButton forgotPasswordButton;
    private JLabel lblUser;
    private JLabel lblPassword;
    private JPanel loginPanel;

    public JPanel getLoginPanel() {
        return loginPanel;
    }

    public Login() {
        okButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                System.out.println("LiorManyak");
            }
        });
    }
}