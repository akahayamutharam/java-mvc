import controller.LoginController;
import view.LoginView;

import javax.swing.*;

public class RetailPOSMain {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginView lv = new LoginView();
            new LoginController(lv);
            lv.setVisible(true);
        });
    }
}
