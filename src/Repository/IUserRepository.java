package Repository;

import Model.User;

public interface IUserRepository extends ICrudRepository<Integer, User>{
    User findByName(String firstName, String lastName);
}
