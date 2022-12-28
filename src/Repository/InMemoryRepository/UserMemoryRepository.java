package Repository.InMemoryRepository;
import Model.Movies;
import Model.Series;
import Model.User;

import Repository.IUserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
public class UserMemoryRepository implements IUserRepository {
    private List<User> allUsers = new ArrayList<>();
    private List<Movies> allMovies = new ArrayList<>();

    private List<Series> allSeries = new ArrayList<>();

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

    @Override
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

    @Override
    public List<Movies> getAllMovies() {
        return new ArrayList<>(allMovies);
    }
    @Override
    public List<Series> getAllSeries() {
        return new ArrayList<>(allSeries);
    }
}
