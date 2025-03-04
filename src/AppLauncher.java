import db_objs.User;
import guis.BankingAppGUI;
import guis.LoginGUI;
import guis.RegisterGUI;

import javax.swing.*;
import java.math.BigDecimal;

public class AppLauncher {
    public static void main(String[] args) {
        //use invokeLater to ake updates to the GUI more thread-safe
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoginGUI().setVisible(true);
//                new RegisterGUI().setVisible(true);
//                new BankingAppGUI(
//                        new User(1, "username", "password", new BigDecimal("20.00"))
//                ).setVisible(true);
            }
        });
    }
}
