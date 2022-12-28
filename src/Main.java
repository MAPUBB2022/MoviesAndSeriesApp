import Controller.ItemController;
import Controller.OrderController;
import Controller.UserController;
import Repository.DatabaseRepo.DatabaseRepository;
import View.View;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) throws IOException, SQLException {
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/moviesandseries", "Guest", "1234");
        DatabaseRepository dbRepo = new DatabaseRepository();
        dbRepo.load(connection);
        UserController uc = new UserController(dbRepo);
        ItemController ic = new ItemController(dbRepo);
        OrderController oc = new OrderController(dbRepo);

        View v = new View(ic, oc, uc);

        v.console();

        Statement stWipe = connection.createStatement();
        stWipe.execute("DELETE FROM User");
        stWipe.execute("DELETE FROM Movies");
        stWipe.execute("DELETE FROM Series");
        dbRepo.store(connection);
    }
}