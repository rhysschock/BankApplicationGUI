package guis;
import db_objs.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* Performs banking functions, such as, depositing, withdrawing, seeing pas transactions and transferring
* extends from BaseFrame, add GUIComponents */

public class BankingAppGUI extends BaseFrame implements ActionListener {
    private JTextField currentBalanceField;
    public JTextField getCurrentBalanceField() {return currentBalanceField;}
    public BankingAppGUI(User user) {
        super("Banking App", user);
    }
    @Override
    protected void addGUIComponents() {
        //create welcome message
        String welcomeMessage = "<html>" + "<body style='text-align:center;'>" + "<b>Hello " + user.getUsername() +
                "!</b><br>" + "What would you like to do today?</body></html>";
        JLabel welcomeMessageLabel = new JLabel(welcomeMessage);
        welcomeMessageLabel.setBounds(0, 20, getWidth()- 10, 40);
        welcomeMessageLabel.setFont(new Font("Dialog", Font.PLAIN, 16));
        welcomeMessageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(welcomeMessageLabel);

        //current balance label
        JLabel CurrentBalanceLabel = new JLabel("Current Balance:");
        CurrentBalanceLabel.setBounds(0, 80, getWidth()- 10, 30);
        CurrentBalanceLabel.setFont(new Font("Dialog", Font.BOLD, 22));
        CurrentBalanceLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(CurrentBalanceLabel);

        //current balance field
        currentBalanceField = new JTextField("$" + user.getCurrentBalance());
        currentBalanceField.setBounds(15, 120, getWidth() - 50, 40);
        currentBalanceField.setFont(new Font("Dialog", Font.BOLD, 28));
        currentBalanceField.setHorizontalAlignment(SwingConstants.RIGHT);
        currentBalanceField.setEditable(false);
        add(currentBalanceField);

        //deposit button
        JButton depositButton = new JButton("Deposit");
        depositButton.setBounds(15, 180, getWidth() - 50, 50);
        depositButton.setFont(new Font("Dialog", Font.BOLD, 22));
        depositButton.setHorizontalAlignment(SwingConstants.CENTER);
        depositButton.addActionListener(this);
        add(depositButton);

        //withdraw button
        JButton withdrawButton = new JButton("Withdraw");
        withdrawButton.setBounds(15, 250, getWidth() - 50, 50);
        withdrawButton.setFont(new Font("Dialog", Font.BOLD, 22));
        withdrawButton.setHorizontalAlignment(SwingConstants.CENTER);
        withdrawButton.addActionListener(this);
        add(withdrawButton);

        //past transaction button
        JButton pastTransactionButton = new JButton("Past Transactions");
        pastTransactionButton.setBounds(15, 320, getWidth() - 50, 50);
        pastTransactionButton.setFont(new Font("Dialog", Font.BOLD, 22));
        pastTransactionButton.setHorizontalAlignment(SwingConstants.CENTER);
        pastTransactionButton.addActionListener(this);
        add(pastTransactionButton);

        //transfer button
        JButton transferButton = new JButton("Transfer");
        transferButton.setBounds(15, 390, getWidth() - 50, 50);
        transferButton.setFont(new Font("Dialog", Font.BOLD, 22));
        transferButton.setHorizontalAlignment(SwingConstants.CENTER);
        transferButton.addActionListener(this);
        add(transferButton);

        //logout button
        JButton logoutButton = new JButton("Logout");
        logoutButton.setBounds(15, 500, getWidth() - 50, 50);
        logoutButton.setFont(new Font("Dialog", Font.BOLD, 22));
        logoutButton.setHorizontalAlignment(SwingConstants.CENTER);
        logoutButton.addActionListener(this);
        add(logoutButton);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String buttonPressed = e.getActionCommand();

        //logout
        if (buttonPressed.equalsIgnoreCase("Logout")) {
            //returns to login page
            new LoginGUI().setVisible(true);
            this.dispose();
        }
        BankingAppDialogue bankingAppDialogue = new BankingAppDialogue(this,user);
        bankingAppDialogue.setTitle(buttonPressed);

        //if the button pressed is deposit, withdraw or transfer
        if(buttonPressed.equalsIgnoreCase("Deposit") || buttonPressed.equalsIgnoreCase("Withdraw")
                || buttonPressed.equalsIgnoreCase("Transfer")) {
            bankingAppDialogue.addCurrentBalanceAndAmount();

            //add action button
            bankingAppDialogue.addActionButton(buttonPressed);

            //transfer action
            if(buttonPressed.equalsIgnoreCase("Transfer")){
                bankingAppDialogue.addUserField();
            }

        }else if(buttonPressed.equalsIgnoreCase("Past Transactions")) {
            bankingAppDialogue.addPastTransactionComponents();
        }
        //make app dialog visible
        bankingAppDialogue.setVisible(true);


    }

}
