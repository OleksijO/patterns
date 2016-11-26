package coffee_machine.dao.impl.jdbc;

import coffee_machine.dao.AccountDao;
import coffee_machine.dao.exception.DaoException;
import coffee_machine.i18n.message.key.EntityKey;
import coffee_machine.i18n.message.key.error.DaoErrorKey;
import coffee_machine.model.entity.Account;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDaoImpl extends AbstractDao<Account> implements AccountDao {

	private static final Logger logger = Logger.getLogger(AccountDaoImpl.class);

	private static final String SELECT_ALL_SQL = "SELECT id, amount FROM account";
	private static final String SELECT_BY_USER_ID_SQL = "SELECT id, amount FROM account "
			+ "INNER JOIN users ON users.account_id = account.id WHERE users.user_id = ?";
	private static final String WHERE_ID = " WHERE id = ?";
	private static final String INSERT_SQL = "INSERT INTO account (amount) VALUES (?);";
	private static final String UPDATE_SQL = "UPDATE account SET amount=? WHERE id=?;";
	private static final String DELETE_SQL = "DELETE FROM account" + WHERE_ID + ";";

	private static final String FIELD_ID = "id";
	private static final String FIELD_AMOUNT = "amount";
	private Connection connection;

	AccountDaoImpl(Connection connection) {
		super(logger, EntityKey.ACCOUNT);
		this.connection = connection;
	}

	@Override
	public Account insert(Account account) {
		if (account == null) {
			throw new DaoException(DaoErrorKey.CAN_NOT_CREATE_EMPTY);
		}
		if (account.getId() != 0) {
			throw new DaoException(DaoErrorKey.CAN_NOT_CREATE_ALREADY_SAVED);
		}

		try (PreparedStatement statement = connection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

			statement.setLong(1, account.getAmount());

			int accountId = executeInsertStatement(statement);

			account.setId(accountId);

		} catch (SQLException e) {
			logErrorAndThrowDaoException(DaoErrorKey.DB_ERROR_WHILE_INSERTING, e);
		}
		return account;
	}

	@Override
	public void update(Account account) {
		if (account == null) {
			throw new DaoException(DaoErrorKey.CAN_NOT_UPDATE_EMPTY);
		}
		if (account.getId() == 0) {
			throw new DaoException(DaoErrorKey.CAN_NOT_UPDATE_UNSAVED);
		}

		try (PreparedStatement statement = connection.prepareStatement(UPDATE_SQL)) {

			statement.setLong(1, account.getAmount());
            statement.setInt(2, account.getId());

			statement.executeUpdate();

		} catch (SQLException e) {
			logErrorAndThrowDaoException(DaoErrorKey.DB_ERROR_WHILE_UPDATING, e);
		}
	}

	@Override
	public List<Account> getAll() {
		try (Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(SELECT_ALL_SQL + ";")) {

			return parseResultSet(resultSet);

		} catch (SQLException e) {
			logErrorAndThrowDaoException(DaoErrorKey.DB_ERROR_WHILE_GETTING_ALL, e);
		}
		throw new InternalError(); // STUB for compiler

	}

	private List<Account> parseResultSet(ResultSet resultSet) throws SQLException {
		List<Account> accountList = new ArrayList<>();
		while (resultSet.next()) {
			Account account = new Account();
			account.setId(resultSet.getInt(FIELD_ID));
			account.setAmount(resultSet.getLong(FIELD_AMOUNT));
			accountList.add(account);
		}
		return accountList;
	}

	@Override
	public Account getById(int id) {
		try (PreparedStatement statement = connection.prepareStatement(SELECT_ALL_SQL + WHERE_ID)) {

			statement.setInt(1, id);
			List<Account> accountList = parseResultSet(statement.executeQuery());
			checkSingleResult(accountList);

			return accountList == null || accountList.isEmpty() ? null : accountList.get(0);

		} catch (SQLException e) {
			logErrorAndThrowDaoException(DaoErrorKey.DB_ERROR_WHILE_GETTING_BY_ID, e);
		}
		throw new InternalError(); // STUB for compiler

	}

	@Override
	public Account getByUserId(int userId) {
		try (PreparedStatement statement = connection.prepareStatement(SELECT_BY_USER_ID_SQL)) {

			statement.setInt(1, userId);
			List<Account> accountList = parseResultSet(statement.executeQuery());
			checkSingleResult(accountList);

			return accountList == null || accountList.isEmpty() ? null : accountList.get(0);

		} catch (SQLException e) {
			logErrorAndThrowDaoException(DaoErrorKey.DB_ERROR_WHILE_GETTING_BY_ID, e);
		}
		throw new InternalError(); // STUB for compiler

	}

	@Override
	public void deleteById(int id) {
		Account account = getById(id);
		if (account == null) {
			return;
		}
		try (PreparedStatement statement = connection.prepareStatement(DELETE_SQL)) {

			statement.setInt(1, id);
			statement.executeUpdate();

		} catch (SQLException e) {
			logErrorAndThrowDaoException(DaoErrorKey.DB_ERROR_WHILE_DELETING_BY_ID, account, e);
		}
	}

}
