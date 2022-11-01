package Controller;

import Model.Order;
import Model.User;

import java.util.List;


public class UserController {
    private List<User> users;

    public UserController(List<User> users) {
        this.users = users;
    }

    public List<User> getUsers() {
        return users;
    }
}
