package guis;

/* This GUI will allow the user to login or launch the register GUI */

import db_objs.MyJDBC;
import db_objs.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginGUI extends BaseFrame{
    public LoginGUI() {
        super("Banking App Login");
    }

    @Override
    protected void addGUIComponents() {
        //create banking app label
        JLabel bankingAppLabel = new JLabel("Banking Application");

        //set the location and size of the gui component
        bankingAppLabel.setBounds(0,20, super.getWidth(),40);

        //change font style
        bankingAppLabel.setFont(new Font("Dialog", Font.BOLD, 32));

        //center text in JLabel
        bankingAppLabel.setHorizontalAlignment(SwingConstants.CENTER);

        //add to gui
        add(bankingAppLabel);

        //username label
        JLabel usernameLabel = new JLabel("Username:");

        //getWidth returns the width of the frame
        usernameLabel.setBounds(20,120, getWidth() - 30,24);

        //change font style
        usernameLabel.setFont(new Font("Dialog", Font.PLAIN, 20));

        add(usernameLabel);

        //create Username Field
        JTextField usernameField = new JTextField();

        usernameField.setBounds(20,160, getWidth() - 50,40);

        usernameField.setFont(new Font("Dialog", Font.PLAIN, 28));

        add(usernameField);

        //add password label
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(20,280, getWidth() - 50,24);
        passwordLabel.setFont(new Font("Dialog", Font.PLAIN, 20));
        add(passwordLabel);

        //add password field
        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(20,320, getWidth() - 50,40);
        passwordField.setFont(new Font("Dialog", Font.PLAIN, 28));
        add(passwordField);

        //create login button
        JButton loginButton = new JButton("Login");
        loginButton.setBounds(20,460, getWidth() - 50,40);
        loginButton.setFont(new Font("Dialog", Font.BOLD, 20));
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                 //get username
                String username = usernameField.getText();

                //get password
                String password = String.valueOf(passwordField.getPassword());

                //validate login
                User user = MyJDBC.validateLogin(username, password);

                //if user is NULL it is invalid other it is a valid account
                if(user != null){
                    //means valid login

                    //dispose this gui
                    LoginGUI.this.dispose();

                    //launch bank app gui
                    BankingAppGUI bankingAppGUI = new BankingAppGUI(user);
                    bankingAppGUI.setVisible(true);

                    //show success dialog
                    JOptionPane.showMessageDialog(bankingAppGUI, "Login Successful!");
                }else{
                    //invalid login
                    JOptionPane.showMessageDialog(LoginGUI.this, "Login Failed!");
                }
            }
        });
        add(loginButton);

        //create register label
        JLabel registerLabel = new JLabel("<html><a href=\"#\">Don't have an account? Register here</a></html>");
        registerLabel.setBounds(0,510, getWidth() - 10,30);
        registerLabel.setFont(new Font("Dialog", Font.PLAIN, 20));
        registerLabel.setHorizontalAlignment(SwingConstants.CENTER);

        registerLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //dispose of this gui
                LoginGUI.this.dispose();

                //launch new registration gui
                new RegisterGUI().setVisible(true);
            }
        });
        add(registerLabel);
    }
}
