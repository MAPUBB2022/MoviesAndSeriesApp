package Repository.DatabaseRepo;
import Model.*;

import Repository.IUserRepository;

import javax.xml.transform.Result;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DatabaseRepository implements IUserRepository {
    private List<User> allUsers = new ArrayList<>();
    private List<Movies> allMovies = new ArrayList<>();
    private List<Series> allSeries = new ArrayList<>();

    private List<Movies> predefinedMovies = new ArrayList<>();

    private List<Series> predefinedSeries = new ArrayList<>();


    @Override
    public void add(User u) {
        allUsers.add((User) u);
    }


    @Override
    public User remove(String id) {
        allUsers.removeIf(u -> u.getId().equals(id));
        return null;
    }

    @Override
    public void update(User newEntity, String id) {
        newEntity.setId(id);

    }

    @Override//gaseste userul in functie de id
    public User find(String id) {
        for (User u : allUsers) {
            if (u.getId().equals(id))
                return u;
        }
        return null;
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(allUsers);
    }

    public List<Movies> getAllMovies(){
        return new ArrayList<>(allMovies);
    }
    public List<Series> getAllSeries(){
        return new ArrayList<>(allSeries);
    }

    @Override
    public List<Movies> getPredefinedMovies() {
        return predefinedMovies;
    }

    @Override
    public List<Series> getPredefinedSeries() {
        return predefinedSeries;
    }

    public void store(Connection connection) throws SQLException//aici adaugam in baza de date
    {
        PreparedStatement stUser = connection.prepareStatement("INSERT INTO User(username, firstname, lastname, password) VALUES (?, ?, ?, ?)");
        PreparedStatement stMovie = connection.prepareStatement("INSERT INTO Movies(username, orderdate, title, rating, year, duration, budget) VALUES (?, ?, ?, ?, ?, ?, ?)");
        PreparedStatement stSeries = connection.prepareStatement("INSERT INTO Series(username, orderdate, title, rating, year, nrOfEpisodes) VALUES (?, ?, ?, ?, ?, ?)");
        for(User u: allUsers)
        {
            stUser.setString(1, u.getId());
            stUser.setString(2, u.getFirstName());
            stUser.setString(3, u.getLastName());
            stUser.setString(4, u.getPassword());
            stUser.executeUpdate();
            stMovie.setString(1, u.getId());
            stSeries.setString(1, u.getId());
            for(Order o : u.getOrders())
            {
                stMovie.setTimestamp(2, Timestamp.valueOf(o.getDate()));
                stSeries.setTimestamp(2, Timestamp.valueOf(o.getDate()));
                for (Series series : o.getSeries())
                {
                    stSeries.setString(3, series.getTitle());
                    stSeries.setDouble(4, series.getRating());
                    stSeries.setInt(5, series.getJahr());
                    stSeries.setInt(6, series.getNumberOfEpisodes());
                    stSeries.executeUpdate();
                }
                for (Movies movies : o.getMovies())
                {
                    stMovie.setString(3, movies.getTitle());
                    stMovie.setDouble(4, movies.getRating());
                    stMovie.setInt(5, movies.getJahr());
                    stMovie.setInt(6, movies.getDuration());
                    stMovie.setDouble(7, movies.getBudget());
                    stMovie.executeUpdate();
                }
            }
        }
        PreparedStatement stNewMovie = connection.prepareStatement("INSERT INTO allMovies(title, rating, year, duration, budget) VALUES (?, ?, ?, ?, ?)");
        PreparedStatement stNewSeries = connection.prepareStatement("INSERT INTO allSeries(title, rating, year, nrOfEpisodes) VALUES (?, ?, ?, ?)");

        for (Movies pm : predefinedMovies)
        {
            stNewMovie.setString(1, pm.getTitle());
            stNewMovie.setDouble(2, pm.getRating());
            stNewMovie.setInt(3, pm.getJahr());
            stNewMovie.setInt(4, pm.getDuration());
            stNewMovie.setDouble(5, pm.getBudget());
            stNewMovie.executeUpdate();
        }

        for (Series ps : predefinedSeries)
        {
            stNewSeries.setString(1, ps.getTitle());
            stNewSeries.setDouble(2, ps.getRating());
            stNewSeries.setInt(3, ps.getJahr());
            stNewSeries.setInt(4, ps.getNumberOfEpisodes());
            stNewSeries.executeUpdate();
        }
    }

    public void load(Connection connection) throws SQLException {//aici scoatem din baza de date
        try(Statement queryUser = connection.createStatement(); Statement queryMS = connection.createStatement();) {
            try (ResultSet res = queryUser.executeQuery("SELECT * FROM User")){
                while(res.next()){
                    String username = res.getString("username");
                    String firstname = res.getString("firstname");
                    String lastname = res.getString("lastname");
                    String password = res.getString("password");
                    List<Order> orders = new ArrayList<>();
                    try (ResultSet resM = queryMS.executeQuery("SELECT orderdate, title, rating, year, duration, budget FROM Movies WHERE username = '%s' ORDER BY orderdate ASC".formatted(username)))
                    {
                        while (resM.next())
                        {
                            LocalDateTime orderDate = resM.getTimestamp(1).toLocalDateTime();
                            String title = resM.getString(2);
                            double rating = resM.getDouble(3);
                            int year = resM.getInt(4);
                            int duration = resM.getInt(5);
                            double budget = resM.getDouble(6);
                            Order o = null;
                            for (Order ord : orders)
                                if (ord.getDate().equals(orderDate)) {
                                    ord.getItems().add(new Movies(title, rating, year, duration, budget));
                                    o = ord;
                                    break;
                                }
                            if (o == null) {
                                List<Item> items = new ArrayList<>();
                                items.add(new Movies(title, rating, year, duration, budget));
                                o = new Order(orderDate, items);
                                orders.add(o);
                            }
                            allMovies.add(new Movies(title, rating, year, duration, budget));
                        }
                    }
                    try (ResultSet resS = queryMS.executeQuery("SELECT orderdate, title, rating, year, nrOfEpisodes FROM Series WHERE username = '%s' ORDER BY orderdate ASC".formatted(username)))
                    {
                        while (resS.next())
                        {
                            LocalDateTime orderDate = resS.getTimestamp(1).toLocalDateTime();
                            String title = resS.getString(2);
                            double rating = resS.getDouble(3);
                            int year = resS.getInt(4);
                            int nrOfEpisodes = resS.getInt(5);
                            Order o = null;
                            for (Order ord : orders)
                                if (ord.getDate().equals(orderDate)) {
                                    ord.getItems().add(new Series(title, rating, year, nrOfEpisodes));
                                    o = ord;
                                    break;
                                }
                            if (o == null) {
                                List<Item> items = new ArrayList<>();
                                items.add(new Series(title, rating, year, nrOfEpisodes));
                                o = new Order(orderDate, items);
                                orders.add(o);
                            }
                            allSeries.add(new Series(title, rating, year, nrOfEpisodes));
                        }
                    }
                    allUsers.add(new User(username, firstname, lastname, password, orders));
                }
            }
        }
        predefinedMovies.clear();
        predefinedSeries.clear();
        try (Statement queryPredefined = connection.createStatement())
        {
            try (ResultSet res = queryPredefined.executeQuery("SELECT * FROM allMovies"))
            {
                while (res.next())
                {
                    String title = res.getString("title");
                    double rating = res.getDouble("rating");
                    int year = res.getInt("year");
                    int duration = res.getInt("duration");
                    double budget = res.getDouble("budget");
                    predefinedMovies.add(new Movies(title, rating, year, duration, budget));
                }
            }
            try (ResultSet res = queryPredefined.executeQuery("SELECT * FROM allSeries"))
            {
                while (res.next())
                {
                    String title = res.getString("title");
                    double rating = res.getDouble("rating");
                    int year = res.getInt("year");
                    int nrOfEpisodes = res.getInt("nrOfEpisodes");
                    predefinedSeries.add(new Series(title, rating, year, nrOfEpisodes));
                }
            }
        }
    }
}


