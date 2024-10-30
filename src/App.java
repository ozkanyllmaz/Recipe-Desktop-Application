import business.TariflerController;
import core.Helper;
import dao.TariflerDao;
import entity.Tarifler;
import view.HomePage;
import view.WelcomePage;

import java.sql.SQLException;

public class App {
    public static void main(String[] args) throws SQLException {
        Helper.setTheme();

        WelcomePage welcomePage = new WelcomePage();

        //HomePage homePage = new HomePage();

    }
}