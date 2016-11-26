package coffee_machine.dao;

import coffee_machine.model.entity.Account;

public interface AccountDao extends GenericDao<Account> {

	Account getByUserId(int userId);

}