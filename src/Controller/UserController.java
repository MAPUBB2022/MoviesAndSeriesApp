package Controller;

import Model.Movies;
import Model.Order;
import Model.Series;
import Model.User;
import Repository.DatabaseRepo.DatabaseRepository;
import Repository.IUserRepository;
import Repository.InMemoryRepository.UserMemoryRepository;
import Controller.OrderController;
import Controller.ItemController;

import java.sql.DriverManager;
import java.time.LocalDateTime;
import java.util.List;

public class UserController {
    private IUserRepository userRepo ;

    public UserController(IUserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public List<User> getUsers() {
        return userRepo.getAll();
    }

    public List<Movies> getMovies(){
        return userRepo.getAllMovies();
    }

    public List<Series> getSeries(){
        return userRepo.getAllSeries();
    }

    public Movies getPredefinedMovie(String title)
    {
        for (Movies movie : userRepo.getPredefinedMovies())
        {
            if (movie.getTitle().equals(title))
                return movie;
        }
        return null;
    }

    public Series getPredefinedSeries(String title)
    {
        for (Series series : userRepo.getPredefinedSeries())
        {
            if (series.getTitle().equals(title))
                return series;
        }
        return null;
    }

    public User FindUserByIDAndPassword(String id, String password){
        for (User u : userRepo.getAll())
            if (u.getId().equals(id) && u.getPassword().equals(password))
                return u;
        return null;
    }

    public void addUser(String id, String firstname, String lastname, String password, List<Order> order){
        userRepo.add(new User(id, firstname, lastname, password, order));
    }
}
