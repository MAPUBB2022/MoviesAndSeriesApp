package Repository;

import Model.Movies;
import Model.Series;
import Model.User;

import java.util.List;

public interface IUserRepository extends ICrudRepository<String, User>{
    User remove(String id);

    User find(String id);

    List<User> getAll();
    List<Movies> getAllMovies();
    List<Series> getAllSeries();

    List<Movies> getPredefinedMovies();

    List<Series> getPredefinedSeries();
}
