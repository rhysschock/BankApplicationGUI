package guis;

/*
    Displays custom dialogue for BankingAppGUI
 */

import db_objs.MyJDBC;
import db_objs.Transaction;
import db_objs.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.ArrayList;


public class BankingAppDialogue extends JDialog implements ActionListener {
    private User user;
    private BankingAppGUI bankingAppGUI;
    private JLabel balanceLabel, enterAmountLabel, enterUserLabel;
    private JTextField enterAmountField, enterUserField;
    private JButton actionButton;
    private JPanel pastTransactionPanel;
    private ArrayList<Transaction> pastTransactions;

    public BankingAppDialogue(BankingAppGUI bankingAppGUI, User user) {
        setSize(400,400);
        setModal(true);
        setLocationRelativeTo(bankingAppGUI);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        setLayout(null);
        this.bankingAppGUI = bankingAppGUI;
        this.user = user;

    }
    public void addCurrentBalanceAndAmount(){
        //balance label
        balanceLabel = new JLabel("Balance: $" + user.getCurrentBalance());
        balanceLabel.setBounds(0,10,getWidth() - 20,20);
        balanceLabel.setFont(new Font("Dialog", Font.BOLD, 16));
        balanceLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(balanceLabel);

        //enter balance label
        enterAmountLabel = new JLabel("Enter Amount:");
        enterAmountLabel.setBounds(0,50,getWidth() - 20,20);
        enterAmountLabel.setFont(new Font("Dialog", Font.BOLD, 16));
        enterAmountLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(enterAmountLabel);

        //enter amount field
        enterAmountField = new JTextField();
        enterAmountField.setBounds(15,80,getWidth() - 50,40);
        enterAmountField.setFont(new Font("Dialog", Font.BOLD, 20));
        enterAmountField.setHorizontalAlignment(SwingConstants.CENTER);
        add(enterAmountField);
    }
    public void addActionButton(String actionButtonType){
        actionButton = new JButton(actionButtonType);
        actionButton.setBounds(15,300,getWidth() - 50,40);
        actionButton.setFont(new Font("Dialog", Font.BOLD, 20));
        actionButton.addActionListener(this);
        add(actionButton);
    }

    public void addUserField(){
        //enter user label
        enterUserLabel = new JLabel("Enter User: ");
        enterUserLabel.setBounds(15,160,getWidth() - 20,20);
        enterUserLabel.setFont(new Font("Dialog", Font.BOLD, 16));
        enterUserLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(enterUserLabel);

        //enter user field
        enterUserField = new JTextField();
        enterUserField.setBounds(15,190,getWidth() - 50,40);
        enterUserField.setFont(new Font("Dialog", Font.BOLD, 20));
        enterUserField.setHorizontalAlignment(SwingConstants.CENTER);
        add(enterUserField);

    }

    public void addPastTransactionComponents(){
        //container where each transaction will be stored
        pastTransactionPanel = new JPanel();

        //make layout 1x1
        pastTransactionPanel.setLayout(new BoxLayout(pastTransactionPanel, BoxLayout.Y_AXIS));

        //add scrollability
        JScrollPane scrollPane = new JScrollPane(pastTransactionPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBounds(0,20,getWidth() - 15,getHeight() - 15);

        //perfrom db call to retreive past transaction info and srtore in array list
        pastTransactions = MyJDBC.getPastTransactions(user);

        //iterate through array list
        for (int i = 0; i < pastTransactions.size(); i++) {
            //store current transaction
            Transaction pastTransaction = pastTransactions.get(i);

            //reate container of past transactions
            JPanel pastTransactionContainer = new JPanel();
            pastTransactionContainer.setLayout(new BorderLayout());

            JLabel transactionTypeLabel = new JLabel(pastTransaction.getTransactionType());
            transactionTypeLabel.setFont(new Font("Dialog", Font.BOLD, 20));

            JLabel transactionAmountLabel = new JLabel(String.valueOf(pastTransaction.getTransactionAmount()));
            transactionAmountLabel.setFont(new Font("Dialog", Font.BOLD, 20));

            JLabel transactionDateLabel = new JLabel(String.valueOf(pastTransaction.getTransactionDate()));
            transactionDateLabel.setFont(new Font("Dialog", Font.BOLD, 20));

            //add container
            pastTransactionContainer.add(transactionTypeLabel, BorderLayout.WEST); //place on west side
            pastTransactionContainer.add(transactionAmountLabel, BorderLayout.EAST); // place on east side
            pastTransactionContainer.add(transactionDateLabel, BorderLayout.SOUTH); // place on south side

            //white background for each container
            pastTransactionContainer.setBackground(Color.WHITE);

            //black border to each transaction container
            pastTransactionContainer.setBorder(BorderFactory.createLineBorder(Color.BLACK));

            //add transaction component to panel
            pastTransactionPanel.add(pastTransactionContainer);
        }

        //add to the dialog
        add(scrollPane);

    }

    private void handleTransaction(String transactionType, float amountVal){
        Transaction transaction;
        if(transactionType.equalsIgnoreCase("Deposit")){
            user.setCurrentBalance(user.getCurrentBalance().add(new BigDecimal(amountVal)));
            //create transaction
            transaction = new Transaction(user.getId(), transactionType, new BigDecimal(amountVal), null);
        }else{
            user.setCurrentBalance(user.getCurrentBalance().subtract(new BigDecimal(amountVal)));
            transaction = new Transaction(user.getId(), transactionType, new BigDecimal(-amountVal), null);
        }

        //update database
        if(MyJDBC.addTransactionToDatabase(transaction) && MyJDBC.updateCurrentBalance(user)){
            //show success dialogue
            JOptionPane.showMessageDialog(this, transactionType + " Successfully!");

            //reset fields
            resetFieldsAndUpdateCurrentBalance();
        }else{
            JOptionPane.showMessageDialog(this, transactionType + " Failed...");
        }
    }

    private void resetFieldsAndUpdateCurrentBalance(){
        //reset field
        enterAmountField.setText("");
        //only appears when transfer is clicked
        if(enterUserField != null){
            enterUserField.setText("");
        }
        //update current balance
        balanceLabel.setText("Balance: $" + user.getCurrentBalance());

        //update current balance on main gui
        bankingAppGUI.getCurrentBalanceField().setText("$" + user.getCurrentBalance());
    }

    private void handleTransfer(User user, String transferredUser, float amount){
        if(MyJDBC.transfer(user, transferredUser, amount)){
            JOptionPane.showMessageDialog(this, "Transfer Successful!");
            resetFieldsAndUpdateCurrentBalance();
        }else{
            JOptionPane.showMessageDialog(this, "Transfer Failed...");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String buttonPressed = e.getActionCommand();

        //get amount
        float amountVal = Float.parseFloat(enterAmountField.getText());

        //pressed deposit
        if(buttonPressed.equalsIgnoreCase("Deposit")){
            handleTransaction(buttonPressed, amountVal);
        }else{
            //pressed withdraw or transfer

            //validate input to make sure withdraw or transfer amount is less than balance
            int result = user.getCurrentBalance().compareTo(BigDecimal.valueOf(amountVal));
            if(result < 0){
                JOptionPane.showMessageDialog(this, buttonPressed + " Error: Amount is more than current balance");
                return;
            }
            if(buttonPressed.equalsIgnoreCase("Withdraw")){
                handleTransaction(buttonPressed, amountVal);
            }else{
                //transfer
                String tranferredUser = enterUserField.getText();

                //handle transfer
                handleTransfer(user, tranferredUser, amountVal);
            }
        }
    }
}
