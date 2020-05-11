package main;

import ui.Login;

import javax.swing.*;

public class Main {
    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        JFrame mainForm = new JFrame("SPA Application");
        mainForm.setContentPane(new Login().getLoginPanel());
        mainForm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainForm.pack();
        mainForm.setVisible(true);
        //mainForm.setSize(600, 400);
        mainForm.setLocationRelativeTo(null);

    }
}
