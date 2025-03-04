package guis;

import db_objs.MyJDBC;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RegisterGUI extends BaseFrame{
    public RegisterGUI() {
        super("Banking App Register");
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
        passwordLabel.setBounds(20,220, getWidth() - 50,24);
        passwordLabel.setFont(new Font("Dialog", Font.PLAIN, 20));
        add(passwordLabel);

        //add password field
        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(20,260, getWidth() - 50,40);
        passwordField.setFont(new Font("Dialog", Font.PLAIN, 28));
        add(passwordField);

        //re-type password label
        JLabel rePasswordLabel = new JLabel("Re-Type Password:");
        rePasswordLabel.setBounds(20,320, getWidth() - 50,40);
        rePasswordLabel.setFont(new Font("Dialog", Font.PLAIN, 20));
        add(rePasswordLabel);

        //re-type password field
        JPasswordField rePasswordField = new JPasswordField();
        rePasswordField.setBounds(20,360, getWidth() - 50,40);
        rePasswordField.setFont(new Font("Dialog", Font.PLAIN, 28));
        add(rePasswordField);

        //create register button
        JButton registerButton = new JButton("Register");
        registerButton.setBounds(20,460, getWidth() - 50,40);
        registerButton.setFont(new Font("Dialog", Font.BOLD, 20));
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //get username
                String username = usernameField.getText();
                //get password
                String password = String.valueOf(passwordField.getPassword());
                //get re password
                String rePassword = String.valueOf(rePasswordField.getPassword());
                //validate the user input
                if(validateUserInput(username,password,rePassword)){
                    //attempt registration to DB
                    if(MyJDBC.register(username, password)){
                        //register success, dispose
                        RegisterGUI.this.dispose();
                        //launch login
                        LoginGUI loginGUI = new LoginGUI();
                        loginGUI.setVisible(true);
                        //create result dialogue
                        JOptionPane.showMessageDialog(loginGUI, "Successfully registered!");
                    }else{
                        JOptionPane.showMessageDialog(RegisterGUI.this, "Error: Username is already taken!");}
                }else{
                    JOptionPane.showMessageDialog(
                            RegisterGUI.this, "Error: Username is too short or\n" + "Passwords dont match"
                    );
                }
            }
        });
        add(registerButton);

        //create login label
        JLabel loginLabel = new JLabel("<html><a href=\"#\">Have an account? Sign-in here</a></html>");
        loginLabel.setBounds(0,510, getWidth() - 10,30);
        loginLabel.setFont(new Font("Dialog", Font.PLAIN, 20));
        loginLabel.setHorizontalAlignment(SwingConstants.CENTER);
        loginLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //dispose of this gui
                RegisterGUI.this.dispose();
                //launch login gui
                new LoginGUI().setVisible(true);
            }
        });
        add(loginLabel);
    }
    private boolean validateUserInput(String username, String password, String rePassword) {
        if(username.length() == 0 || password.length() == 0 || rePassword.length() == 0) return false;

        //username has to be atleast 6 characters
        if (username.length() < 6) return false;

        //password and repassword must be same
        if (!password.equals(rePassword)) return false;

        //passes validation
        return true;
    }
}
