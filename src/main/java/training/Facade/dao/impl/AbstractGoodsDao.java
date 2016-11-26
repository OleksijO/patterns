package coffee_machine.dao.impl.jdbc;

import org.apache.log4j.Logger;

/**
 * Created by oleksij.onysymchuk@gmail on 15.11.2016.
 */
abstract class AbstractGoodsDao<T> extends AbstractDao<T> {

	protected static final String SELECT_ALL_FROM_GOODS_SQL = "SELECT abstract_goods.id, name, price, quantity %s FROM abstract_goods ";
	protected static final String UPDATE_GOODS_SQL = "UPDATE abstract_goods SET name = ?, price = ?, quantity = ? WHERE id = ?; ";
	protected static final String UPDATE_GOODS_QUANTITY_SQL = "UPDATE abstract_goods SET quantity = ? WHERE id = ?; ";
	protected static final String INSERT_GOODS_SQL = "INSERT INTO abstract_goods (name, price, quantity) VALUES (?, ?, ?); ";
	protected static final String DELETE_GOODS_SQL = "DELETE FROM abstract_goods WHERE id = ?; ";

	public AbstractGoodsDao(Logger logger, String errorMessageEntityPrefix) {
		super(logger, errorMessageEntityPrefix);
	}

}
