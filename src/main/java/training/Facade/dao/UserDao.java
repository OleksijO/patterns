package coffee_machine.dao;

import coffee_machine.model.entity.user.User;

public interface UserDao extends GenericDao<User> {

	User getUserByLogin(String login);

}
