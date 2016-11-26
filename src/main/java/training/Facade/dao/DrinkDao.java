package coffee_machine.dao;

import coffee_machine.model.entity.goods.Drink;

import java.util.List;

public interface DrinkDao extends GenericDao<Drink> {

	List<Drink> getAllFromList(List<Drink> baseDrinks);

	void updateQuantityAllInList(List<Drink> baseDrinks);

	List<Drink> getAllByIds(List<Integer> drinkIds);

	void updateQuantity(Drink drink);
}