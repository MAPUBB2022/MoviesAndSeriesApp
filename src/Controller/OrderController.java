package Controller;

import Model.Item;
import Model.Order;
import Model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import Controller.ItemController;
import Controller.UserController;
import Repository.IUserRepository;

public class OrderController {
    private IUserRepository userRepo;

    public OrderController(IUserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public List<Order> getOrders() {
        List<Order> orders = new ArrayList<>();
        for (User u : userRepo.getAll())
            for (Order o : u.getOrders())
                if (!orders.contains(o))
                    orders.add(o);
        return orders;
    }

    public void addOrder(String userId, LocalDateTime orderDate, List<Item> items) {
        User u = userRepo.find(userId);
        if (u == null)
            return;
        u.getOrders().add(new Order(orderDate, items));
    }

}
