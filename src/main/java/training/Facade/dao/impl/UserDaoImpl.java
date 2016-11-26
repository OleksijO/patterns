package coffee_machine.dao.impl.jdbc;

import coffee_machine.dao.AccountDao;
import coffee_machine.dao.UserDao;
import coffee_machine.dao.exception.DaoException;
import coffee_machine.i18n.message.key.EntityKey;
import coffee_machine.i18n.message.key.error.DaoErrorKey;
import coffee_machine.model.entity.Account;
import coffee_machine.model.entity.user.User;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl extends AbstractUserDao<User> implements UserDao {

	private static final Logger logger = Logger.getLogger(UserDaoImpl.class);

	private static final String WHERE_ABSTRACT_USER_EMAIL = " WHERE abstract_user.email = ?";
	private static final String WHERE_ABSTRACT_USER_ID = " WHERE abstract_user.id = ?";

	private static final String SELECT_ALL_SQL = String.format(SELECT_ALL_FROM_ABSTRACT_USER_SQL,
			", users.account_id, account.amount") + " INNER JOIN users ON abstract_user.id = users.user_id "
			+ " LEFT JOIN account ON users.account_id = account.id ";
	private static final String UPDATE_SQL = UPDATE_ABSTRACT_USER_SQL
			+ " UPDATE account SET amount = ? WHERE id = ?;";
	private static final String INSERT_SQL = "INSERT INTO users (user_id, account_id) VALUES (?, ?);";
	private static final String DELETE_SQL = DELETE_ABSTRACT_USER_SQL + " ";

	private static final String FIELD_LOGIN = "email";
	private static final String FIELD_PASSWORD = "password";
	private static final String FIELD_FULL_NAME = "full_name";
	private static final String FIELD_ACCOUNT_ID = "account_id";
	private static final String FIELD_ACCOUNT_AMOUNT = "amount";

	private Connection connection;
	private AccountDao accountDao;

	public UserDaoImpl(Connection connection, AccountDao accountDao) {
		super(logger, EntityKey.USER);
		this.connection = connection;
		this.accountDao = accountDao;
	}

	@Override
	public User insert(User user) {
		if (user == null) {
			throw new DaoException(DaoErrorKey.CAN_NOT_CREATE_EMPTY);
		}
		if (user.getId() != 0) {
			throw new DaoException(DaoErrorKey.CAN_NOT_CREATE_ALREADY_SAVED);
		}

		int accountId = accountDao.insert(user.getAccount()).getId();

		try (PreparedStatement statementForAbstractUser = connection.prepareStatement(INSERT_ABSTRACT_USER_SQL,
				Statement.RETURN_GENERATED_KEYS);
				PreparedStatement statementForUser = connection.prepareStatement(INSERT_SQL)) {

			statementForAbstractUser.setString(1, user.getEmail());
			statementForAbstractUser.setString(2, user.getPassword());
			statementForAbstractUser.setString(3, user.getFullName());

			int abstractUserId = executeInsertStatement(statementForAbstractUser);;
			user.setId(abstractUserId);
			statementForUser.setInt(1, abstractUserId);
			statementForUser.setInt(2, accountId);
			statementForUser.executeUpdate();

		} catch (SQLException e) {
			logErrorAndThrowDaoException(DaoErrorKey.DB_ERROR_WHILE_INSERTING, user, e);
		}
		return user;
	}

	@Override
	public void update(User user) {
		if (user == null) {
			throw new DaoException(DaoErrorKey.CAN_NOT_UPDATE_EMPTY);
		}
		if (user.getId() == 0) {
			throw new DaoException(DaoErrorKey.CAN_NOT_UPDATE_UNSAVED);
		}
		try (PreparedStatement statement = connection.prepareStatement(UPDATE_SQL);) {

			statement.setString(1, user.getEmail());
			statement.setString(2, user.getPassword());
			statement.setString(3, user.getFullName());
			statement.setInt(4, user.getId());
			statement.setLong(5, user.getId());
			statement.setInt(6, user.getAccount().getId());
			System.out.println(UPDATE_SQL);
			statement.executeUpdate();

		} catch (SQLException e) {
			logErrorAndThrowDaoException(DaoErrorKey.DB_ERROR_WHILE_UPDATING, user, e);
		}

	}

	@Override
	public List<User> getAll() {
		try (Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(SELECT_ALL_SQL)) {

			return parseResultSet(resultSet);

		} catch (SQLException e) {
			logErrorAndThrowDaoException(DaoErrorKey.DB_ERROR_WHILE_GETTING_ALL, e);
		}
		throw new InternalError(); // STUB for compiler

	}

	private List<User> parseResultSet(ResultSet resultSet) throws SQLException {
		List<User> userList = new ArrayList<>();
		while (resultSet.next()) {
			User user = new User();
			user.setId(resultSet.getInt(FIELD_ID));
			user.setFullName(resultSet.getString(FIELD_FULL_NAME));
			user.setEmail(resultSet.getString(FIELD_LOGIN));
			user.setPassword(resultSet.getString(FIELD_PASSWORD));
			Account account = new Account();
			account.setId(resultSet.getInt(FIELD_ACCOUNT_ID));
			account.setAmount(resultSet.getLong(FIELD_ACCOUNT_AMOUNT));
			user.setAccount(account);
			userList.add(user);
		}
		return userList;
	}

	@Override
	public User getById(int id) {
		try (PreparedStatement statement = connection.prepareStatement(SELECT_ALL_SQL + WHERE_ABSTRACT_USER_ID)) {

			statement.setInt(1, id);
			List<User> userList = parseResultSet(statement.executeQuery());
			checkSingleResult(userList);

			return userList == null || userList.isEmpty() ? null : userList.get(0);

		} catch (SQLException e) {
			logErrorAndThrowDaoException(DaoErrorKey.DB_ERROR_WHILE_GETTING_BY_ID, e);
		}
		throw new InternalError(); // STUB for compiler

	}

	@Override
	public void deleteById(int id) {
		User user = getById(id);
		if (user == null) {
			return;
		}
		try (PreparedStatement statement = connection.prepareStatement(DELETE_SQL)) {

			statement.setInt(1, id);
			statement.executeUpdate();

		} catch (SQLException e) {
			logErrorAndThrowDaoException(DaoErrorKey.DB_ERROR_WHILE_DELETING_BY_ID, user, e);
		}
		accountDao.deleteById(user.getAccount().getId());
	}

	@Override
	public User getUserByLogin(String login) {
		try (PreparedStatement statement = connection.prepareStatement(SELECT_ALL_SQL + WHERE_ABSTRACT_USER_EMAIL)) {

			statement.setString(1, login);
			List<User> userList = parseResultSet(statement.executeQuery());
			checkSingleResult(userList);

			return userList == null || userList.isEmpty() ? null : userList.get(0);

		} catch (SQLException e) {
			logErrorAndThrowDaoException(DaoErrorKey.DB_ERROR_WHILE_GETTING_BY_LOGIN, login, e);
		}
		throw new InternalError(); // STUB for compiler

	}

}
