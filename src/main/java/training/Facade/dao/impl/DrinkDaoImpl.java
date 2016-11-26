package coffee_machine.dao.impl.jdbc;

import coffee_machine.dao.DrinkDao;
import coffee_machine.dao.exception.DaoException;
import coffee_machine.i18n.message.key.EntityKey;
import coffee_machine.i18n.message.key.error.DaoErrorKey;
import coffee_machine.model.entity.goods.Addon;
import coffee_machine.model.entity.goods.Drink;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class DrinkDaoImpl extends AbstractGoodsDao<Drink> implements DrinkDao {
	private static final Logger logger = Logger.getLogger(DrinkDaoImpl.class);

	private static final String WHERE_GOODS_ID = " WHERE abstract_goods.id = ?";

	private static final String SELECT_ALL_SQL = String.format(SELECT_ALL_FROM_GOODS_SQL, "")
			+ " INNER JOIN drink ON abstract_goods.id = drink.id ";
	private static final String UPDATE_SQL = UPDATE_GOODS_SQL + "";
	private static final String INSERT_SQL = "INSERT INTO drink (id) VALUES (?);";
	private static final String DELETE_SQL = DELETE_GOODS_SQL + "";

	private static final String SELECT_ADDON_SET_SQL = "SELECT addon_id, name, price, quantity " + "FROM abstract_goods "
			+ "INNER JOIN drink_addons ON addon_id=abstract_goods.id " + "WHERE drink_id = ? ;";
	private static final String INSERT_ADDON_SQL = "INSERT INTO drink_addons (drink_id, addon_id) VALUES (?,?); ";
	private static final String DELETE_ADDON_FROM_SET_SQL = "DELETE FROM drink_addons WHERE drink_id = ?; ";

	private static final String FIELD_NAME = "name";
	private static final String FIELD_PRICE = "price";
	private static final String FIELD_QUANTITY = "quantity";
	private static final String FIELD_ADDON_ID = "addon_id";

	private Connection connection;

	public DrinkDaoImpl(Connection connection) {
		super(logger, EntityKey.ADDON);
		this.connection = connection;
	}

	@Override
	public Drink insert(Drink drink) {
		if (drink == null) {
			throw new DaoException(DaoErrorKey.CAN_NOT_CREATE_EMPTY);
		}
		if (drink.getId() != 0) {
			throw new DaoException(DaoErrorKey.CAN_NOT_CREATE_ALREADY_SAVED);
		}

		try (PreparedStatement statementForGoods = connection.prepareStatement(INSERT_GOODS_SQL,
				Statement.RETURN_GENERATED_KEYS);
				PreparedStatement statementForDrink = connection.prepareStatement(INSERT_SQL);
				PreparedStatement statementForInsertAddonToSet = connection.prepareStatement(INSERT_ADDON_SQL)) {

			statementForGoods.setString(1, drink.getName());
			statementForGoods.setLong(2, drink.getPrice());
			statementForGoods.setInt(3, drink.getQuantity());

			int drinkId = executeInsertStatement(statementForGoods);
			drink.setId(drinkId);
			statementForDrink.setInt(1, drinkId);

			statementForDrink.executeUpdate();

			Set<Addon> addons = drink.getAddons();
			if ((addons != null) && (!addons.isEmpty())) {
				for (Addon addon : addons) {
					statementForInsertAddonToSet.setInt(1, drinkId);
					statementForInsertAddonToSet.setInt(2, addon.getId());
					statementForInsertAddonToSet.executeUpdate();
				}
				;
			}

		} catch (SQLException e) {
			logErrorAndThrowDaoException(DaoErrorKey.DB_ERROR_WHILE_INSERTING, drink, e);
		}
		return drink;
	}

	@Override
	public void updateQuantity(Drink drink) {
		if (drink == null) {
			throw new DaoException(DaoErrorKey.CAN_NOT_UPDATE_EMPTY);
		}
		if (drink.getId() == 0) {
			throw new DaoException(DaoErrorKey.CAN_NOT_UPDATE_UNSAVED);
		}
		try (PreparedStatement statement = connection.prepareStatement(UPDATE_GOODS_QUANTITY_SQL)) {

			statement.setInt(1, drink.getQuantity());
			statement.setInt(2, drink.getId());
			statement.executeUpdate();

		} catch (SQLException e) {
			logErrorAndThrowDaoException(DaoErrorKey.DB_ERROR_WHILE_UPDATING, drink, e);
		}
	}

	@Override
	public void update(Drink drink) {
		if (drink == null) {
			throw new DaoException(DaoErrorKey.CAN_NOT_UPDATE_EMPTY);
		}
		if (drink.getId() == 0) {
			throw new DaoException(DaoErrorKey.CAN_NOT_UPDATE_UNSAVED);
		}

		try (PreparedStatement statement = connection.prepareStatement(UPDATE_SQL);
				PreparedStatement statementForInsertAddonToSet = connection.prepareStatement(INSERT_ADDON_SQL);
				PreparedStatement statementForDeleteAddonsFromSet = connection
						.prepareStatement(DELETE_ADDON_FROM_SET_SQL)) {

			statement.setString(1, drink.getName());
			statement.setLong(2, drink.getPrice());
			statement.setInt(3, drink.getQuantity());
			statement.setInt(4, drink.getId());
			statement.executeUpdate();

			statementForDeleteAddonsFromSet.setInt(1, drink.getId());
			statementForDeleteAddonsFromSet.executeUpdate();

			Set<Addon> addons = drink.getAddons();
			if ((addons != null) && (!addons.isEmpty())) {
				for (Addon addon : addons) {
					statementForInsertAddonToSet.setInt(1, drink.getId());
					statementForInsertAddonToSet.setInt(2, addon.getId());
					statementForInsertAddonToSet.executeUpdate();
				}
			}
		} catch (SQLException e) {
			logErrorAndThrowDaoException(DaoErrorKey.DB_ERROR_WHILE_UPDATING, drink, e);
		}
	}

	@Override
	public List<Drink> getAll() {
		try (Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(SELECT_ALL_SQL)) {

			return parseResultSet(resultSet);

		} catch (SQLException e) {
			logErrorAndThrowDaoException(DaoErrorKey.DB_ERROR_WHILE_GETTING_ALL, e);
		}
		throw new InternalError(); // STUB for compiler
	}

	private List<Drink> parseResultSet(ResultSet resultSet) throws SQLException {
		List<Drink> drinkList = new ArrayList<>();
		while (resultSet.next()) {
			Drink drink = new Drink();
			drink.setId(resultSet.getInt(FIELD_ID));
			drink.setName(resultSet.getString(FIELD_NAME));
			drink.setPrice(resultSet.getLong(FIELD_PRICE));
			drink.setQuantity(resultSet.getInt(FIELD_QUANTITY));
			drink.setAddons(getAddonSet(drink.getId()));
			drinkList.add(drink);
		}
		return drinkList;
	}

	private Set<Addon> getAddonSet(int drinkId) {
		try (PreparedStatement statement = connection.prepareStatement(SELECT_ADDON_SET_SQL)) {

			statement.setInt(1, drinkId);
			Set<Addon> addonSet = parseAddonSetResultSet(statement.executeQuery());
			return addonSet;

		} catch (SQLException e) {
			logErrorAndThrowDaoException(DaoErrorKey.DB_ERROR_WHILE_UPDATING, drinkId, e);
		}
		throw new InternalError(); // STUB for compiler
	}

	private Set<Addon> parseAddonSetResultSet(ResultSet resultSet) throws SQLException {
		Set<Addon> addonSet = new TreeSet<>();
		while (resultSet.next()) {
			Addon addon = new Addon();
			addon.setId(resultSet.getInt(FIELD_ADDON_ID));
			addon.setName(resultSet.getString(FIELD_NAME));
			addon.setPrice(resultSet.getLong(FIELD_PRICE));
			addon.setQuantity(resultSet.getInt(FIELD_QUANTITY));
			addonSet.add(addon);
		}
		return addonSet;
	}

	@Override
	public Drink getById(int id) {
		try (PreparedStatement statement = connection.prepareStatement(SELECT_ALL_SQL + WHERE_GOODS_ID)) {

			statement.setInt(1, id);
			List<Drink> drinkList = parseResultSet(statement.executeQuery());
			checkSingleResult(drinkList);

			return drinkList == null || drinkList.isEmpty() ? null : drinkList.get(0);

		} catch (SQLException e) {
			logErrorAndThrowDaoException(DaoErrorKey.DB_ERROR_WHILE_GETTING_BY_ID, e);
		}
		throw new InternalError(); // STUB for compiler
	}

	@Override
	public void deleteById(int id) {
		Drink drink = getById(id);
		if (drink == null) {
			return;
		}
		try (PreparedStatement statement = connection.prepareStatement(DELETE_SQL);
				PreparedStatement statementForDeleteAddonsFromSet = connection
						.prepareStatement(DELETE_ADDON_FROM_SET_SQL)) {

			statement.setInt(1, id);
			statement.executeUpdate();

			statementForDeleteAddonsFromSet.setInt(1, drink.getId());
			statementForDeleteAddonsFromSet.executeUpdate();

		} catch (SQLException e) {
			logErrorAndThrowDaoException(DaoErrorKey.DB_ERROR_WHILE_DELETING_BY_ID, drink, e);
		}
	}

	@Override
	public List<Drink> getAllFromList(List<Drink> drinks) {
		List<Drink> updatedDrinks = new ArrayList<>();
		drinks.forEach(drink -> updatedDrinks.add(getById(drink.getId())));
		return updatedDrinks;
	}

	@Override
	public List<Drink> getAllByIds(List<Integer> drinkIds) {
		List<Drink> drinks = new ArrayList<>();
		drinkIds.forEach(id -> drinks.add(getById(id)));
		return drinks;
	}

	@Override
	public void updateQuantityAllInList(List<Drink> drinks) {
		drinks.forEach(this::updateQuantity);
	}

}
