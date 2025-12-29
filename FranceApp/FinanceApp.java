import controller.FinanceController;
import dao.AccountDAO;
import dao.DBHelper;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import service.AccountService;
import view.MainView;

public class FinanceApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                DBHelper db = new DBHelper();
                AccountDAO accountDAO = new AccountDAO(db);
                AccountService accountService = new AccountService(accountDAO);

                DefaultTableModel model = new DefaultTableModel();
                FinanceController controller = new FinanceController(accountService, model);

                MainView view = new MainView(controller, model);
                view.setVisible(true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Startup error: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
}
