package coffee_machine.dao.impl.jdbc;

import coffee_machine.dao.HistoryRecordDao;
import coffee_machine.dao.exception.DaoException;
import coffee_machine.i18n.message.key.EntityKey;
import coffee_machine.i18n.message.key.error.DaoErrorKey;
import coffee_machine.model.entity.HistoryRecord;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HistoryRecordDaoImpl extends AbstractDao<HistoryRecord> implements HistoryRecordDao {

	private static final Logger logger = Logger.getLogger(HistoryRecordDaoImpl.class);

	private static final String SELECT_ALL_SQL = "SELECT id, user_id, date_time, order_description, amount  FROM history_record";
	private static final String SELECT_BY_USER_ID_SQL = "SELECT id, user_id, date_time, order_description, amount FROM history_record "
			+ " WHERE users.user_id = ?";
	private static final String WHERE_ID = " WHERE id = ?";
	private static final String INSERT_SQL = "INSERT INTO history_record (user_id, date_time, order_description, amount ) VALUES (?,?,?,?);";
	private static final String UPDATE_SQL = "UPDATE history_record SET user_id=?, date_time=?, order_description=?, amount=?  WHERE id=?;";
	private static final String DELETE_SQL = "DELETE FROM history_record" + WHERE_ID + ";";

	private static final String FIELD_USER_ID = "user_id";
	private static final String FIELD_DATE_TIME = "date_time";
	private static final String FIELD_ORDER_DESCRIPTION = "order_description";
	private static final String FIELD_AMOUNT = "amount";
	private Connection connection;

	HistoryRecordDaoImpl(Connection connection) {
		super(logger, EntityKey.HISTORY_RECORD);
		this.connection = connection;
	}

	@Override
	public HistoryRecord insert(HistoryRecord historyRecord) {
		if (historyRecord == null) {
			throw new DaoException(DaoErrorKey.CAN_NOT_CREATE_EMPTY);
		}
		if (historyRecord.getId() != 0) {
			throw new DaoException(DaoErrorKey.CAN_NOT_CREATE_ALREADY_SAVED);
		}

		try (PreparedStatement statement = connection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

			statement.setInt(1, historyRecord.getUserId());
			statement.setTimestamp(2, toTimestamp(historyRecord.getDate()));
			statement.setString(3, historyRecord.getOrderDescription());
			statement.setLong(4, historyRecord.getAmount());

			int historyRecordId = executeInsertStatement(statement);

			historyRecord.setId(historyRecordId);

		} catch (SQLException e) {
			logErrorAndThrowDaoException(DaoErrorKey.DB_ERROR_WHILE_INSERTING, e);
		}
		return historyRecord;
	}

	@Override
	public void update(HistoryRecord historyRecord) {
		if (historyRecord == null) {
			throw new DaoException(DaoErrorKey.CAN_NOT_UPDATE_EMPTY);
		}
		if (historyRecord.getId() == 0) {
			throw new DaoException(DaoErrorKey.CAN_NOT_UPDATE_UNSAVED);
		}

		try (PreparedStatement statement = connection.prepareStatement(UPDATE_SQL)) {
			statement.setInt(1, historyRecord.getUserId());
			statement.setTimestamp(2, toTimestamp(historyRecord.getDate()));
			statement.setString(3, historyRecord.getOrderDescription());
			statement.setLong(4, historyRecord.getAmount());
            statement.setInt(5, historyRecord.getId());

			statement.executeUpdate();

		} catch (SQLException e) {
			logErrorAndThrowDaoException(DaoErrorKey.DB_ERROR_WHILE_UPDATING, e);
		}
	}

	private Timestamp toTimestamp(Date date) {
		return new Timestamp(date.getTime());
	}

	@Override
	public List<HistoryRecord> getAll() {
		try (Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(SELECT_ALL_SQL + ";")) {

			return parseResultSet(resultSet);

		} catch (SQLException e) {
			logErrorAndThrowDaoException(DaoErrorKey.DB_ERROR_WHILE_GETTING_ALL, e);
		}
		throw new InternalError(); // STUB for compiler

	}

	private List<HistoryRecord> parseResultSet(ResultSet resultSet) throws SQLException {
		List<HistoryRecord> historyRecordList = new ArrayList<>();
		while (resultSet.next()) {
			HistoryRecord historyRecord = new HistoryRecord();
			historyRecord.setId(resultSet.getInt(FIELD_ID));
			historyRecord.setUserId(resultSet.getInt(FIELD_USER_ID));
			historyRecord.setDate(toDate(resultSet.getTimestamp(FIELD_DATE_TIME)));
			historyRecord.setOrderDescription(resultSet.getString(FIELD_ORDER_DESCRIPTION));
			historyRecord.setAmount(resultSet.getLong(FIELD_AMOUNT));
			historyRecordList.add(historyRecord);
		}
		return historyRecordList;
	}
	private Date toDate(Timestamp timestamp){
		return new Date(timestamp.getTime());
	}

	@Override
	public HistoryRecord getById(int id) {
		try (PreparedStatement statement = connection.prepareStatement(SELECT_ALL_SQL + WHERE_ID)) {

			statement.setInt(1, id);
			List<HistoryRecord> historyRecordList = parseResultSet(statement.executeQuery());
			checkSingleResult(historyRecordList);

			return historyRecordList == null || historyRecordList.isEmpty() ? null : historyRecordList.get(0);

		} catch (SQLException e) {
			logErrorAndThrowDaoException(DaoErrorKey.DB_ERROR_WHILE_GETTING_BY_ID, e);
		}
		throw new InternalError(); // STUB for compiler

	}

	@Override
	public void deleteById(int id) {
		HistoryRecord historyRecord = getById(id);
		if (historyRecord == null) {
			return;
		}
		try (PreparedStatement statement = connection.prepareStatement(DELETE_SQL)) {

			statement.setInt(1, id);
			statement.executeUpdate();

		} catch (SQLException e) {
			logErrorAndThrowDaoException(DaoErrorKey.DB_ERROR_WHILE_DELETING_BY_ID, historyRecord, e);
		}
	}

}
