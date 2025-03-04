package db_objs;

/* JBDC class is used ot interact with MySQL Database. It will retrieve and update database */

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;


public class MyJDBC {
    //database configs
    private static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/bankapp";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "jozrez-sejqip-xaDbu0";

    public static User validateLogin(String username, String password) {
        try {
            //establish connection
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

            //create query
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM bankapp.users WHERE username = ? " + "AND password = ?");
            //replace ? with values
            //parameter index referring that 1 is the first ? and 2 is the second ?
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();

            //next() returns boolean
            //true - query returned data and now set points to the first row
            //false - query returned no data and result set equals to NULL
            if (resultSet.next()) {
                //success, get id
                int userId = resultSet.getInt("id");

                //get current balance
                BigDecimal currentBalance = resultSet.getBigDecimal("current_balance");

                //return user object
                return new User(userId, username, password, currentBalance);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //not a valid user
        return null;
    }

    public static boolean register(String username, String password) {
        try {
            //check if username has been taken
            if (!checkUser(username)) {
                Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "INSERT INTO bankapp.users(username, password, current_balance)" + "VALUES (?, ?, ?)"
                );
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);
                preparedStatement.setBigDecimal(3, new BigDecimal(0));

                preparedStatement.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static boolean checkUser(String username) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM bankapp.users WHERE username = ?"
            );
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            //means that the query returned no data meaning the username is available
            if (!resultSet.next()) {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static boolean addTransactionToDatabase(Transaction transaction) {
        try{
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            PreparedStatement insertTransaction = connection.prepareStatement(
                   "INSERT transactions(user_id, transaction_type, transaction_amount, transaction_date)" +
                           "VALUES(?, ?, ?, NOW()) "
            );

            insertTransaction.setInt(1, transaction.getUserId());
            insertTransaction.setString(2, transaction.getTransactionType());
            insertTransaction.setBigDecimal(3, transaction.getTransactionAmount());

            //update database
            insertTransaction.executeUpdate();

            return true;

        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    //true - update balance success
    // false - update balance fail
    public static boolean updateCurrentBalance(User user) {
        try{
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            PreparedStatement updateBalance = connection.prepareStatement(
                    "UPDATE bankapp.users SET current_balance = ? WHERE id = ?"
            );

            updateBalance.setBigDecimal(1, user.getCurrentBalance());
            updateBalance.setInt(2, user.getId());
            updateBalance.executeUpdate();
            return true;

        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

    //true - transfer successful
    // false - transfer unsuccessful
    public static boolean transfer(User user, String transferredUsername, float transferAmount) {
        try{
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            PreparedStatement queryUser = connection.prepareStatement(
                  "SELECT * FROM bankapp.users WHERE username = ? "
            );
            queryUser.setString(1, transferredUsername);
            ResultSet resultSet = queryUser.executeQuery();

            while (resultSet.next()) {
                //perform transfer
                User transferredUser = new User(
                        resultSet.getInt("id"),
                        transferredUsername,
                        resultSet.getString("password"),
                        resultSet.getBigDecimal("current_balance")
                );

                //create transaction
                Transaction transferTransaction = new Transaction(
                        user.getId(),
                        "Transfer",
                        new BigDecimal(-transferAmount),
                        null
                );

                Transaction receivedTransaction = new Transaction(
                        transferredUser.getId(),
                        "Transfer",
                        new BigDecimal(transferAmount),
                        null
                );

                //update transfer user
                transferredUser.setCurrentBalance(transferredUser.getCurrentBalance().add(BigDecimal.valueOf(transferAmount)));
                updateCurrentBalance(transferredUser);

                //update current balance
                user.setCurrentBalance(user.getCurrentBalance().subtract(BigDecimal.valueOf(transferAmount)));
                updateCurrentBalance(user);

                //add transactions to the database
                addTransactionToDatabase(transferTransaction);
                addTransactionToDatabase(receivedTransaction);

                return true;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

    //get all transactions (past transactions)
    public static ArrayList<Transaction> getPastTransactions(User user) {
        ArrayList<Transaction> pastTransactions = new ArrayList<>();
        try{
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            PreparedStatement selectAllTransaction = connection.prepareStatement(
                    "SELECT * FROM bankapp.transactions WHERE user_id = ?"
            );
            selectAllTransaction.setInt(1, user.getId());
            ResultSet resultSet = selectAllTransaction.executeQuery();
            while (resultSet.next()) {
                Transaction transaction = new Transaction(
                        user.getId(),
                        resultSet.getString("transaction_type"),
                        resultSet.getBigDecimal("transaction_amount"),
                        resultSet.getDate("transaction_date")
                );

                //store into array
                pastTransactions.add(transaction);
            }

        }catch(SQLException e){
            e.printStackTrace();
        }

        return pastTransactions;
    }
}