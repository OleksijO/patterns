package coffee_machine.dao.impl.jdbc;

import coffee_machine.dao.*;
import coffee_machine.dao.exception.DaoException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DaoFactoryImpl implements coffee_machine.dao.DaoFactory {
	private DataSource dataSource = JdbcPooledDataSource.getInstance();

	private static class InstanceHolder {
		private static final DaoFactory instance = new DaoFactoryImpl();
	}

	public static DaoFactory getInstance() {
		return InstanceHolder.instance;
	}

	@Override
	public AbstractConnection getConnection() {
		Connection connection;
		try {
			connection = dataSource.getConnection();
		} catch (SQLException e) {
			// TODO log
			e.printStackTrace();
			throw new DaoException(e);
		}

		if (connection == null) {
			// TODO log
			throw new DaoException("SQL connection can not be null.");
		}

		return new AbstractConnectionImpl(connection);
	}

	@Override
	public UserDao getUserDao(AbstractConnection connection) {
		checkConnection(connection);
		Connection sqlConnection = getSqlConnection(connection);
		return new UserDaoImpl(sqlConnection, new AccountDaoImpl(sqlConnection));
	}

	private Connection getSqlConnection(AbstractConnection connection) {

		return ((AbstractConnectionImpl) connection).getConnection();
	}

	private void checkConnection(AbstractConnection connection) {
		if (connection == null) {
			throw new DaoException("Connection can not be null.");
		}
		if (!(connection instanceof AbstractConnectionImpl)) {
			throw new DaoException("Connection is not a AbstractConnectionIml for JDBC.");
		}

	}

	@Override
	public AdminDao getAdminDao(AbstractConnection connection) {
		checkConnection(connection);
		return new AdminDaoImpl(getSqlConnection(connection));
	}

	@Override
	public DrinkDao getDrinkDao(AbstractConnection connection) {
		checkConnection(connection);
		return new DrinkDaoImpl(getSqlConnection(connection));
	}

	@Override
	public AddonDao getAddonDao(AbstractConnection connection) {
		checkConnection(connection);
		return new AddonDaoImpl(getSqlConnection(connection));
	}

	@Override
	public AccountDao getAccountDao(AbstractConnection connection) {
		checkConnection(connection);
		return new AccountDaoImpl(getSqlConnection(connection));
	}

    @Override
    public HistoryRecordDao getHistoryRecordDao(AbstractConnection connection) {
        checkConnection(connection);
        return new HistoryRecordDaoImpl(getSqlConnection(connection));
    }
}
