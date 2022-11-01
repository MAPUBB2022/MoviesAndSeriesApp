package Model;

import java.util.List;

public class User {
    private int id;
private String firstName;
private String lastName;
private List<Order> orders;

    public User(int id, String firstName, String lastName, List<Order> orders) {
        this.id=id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.orders = orders;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public double calculatePriceForAllOrders(){
        double sum = 0;
        for (Order o : orders)
            sum += o.calculatePriceForOrder();
        return sum;
    }
}
