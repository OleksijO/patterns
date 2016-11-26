package coffee_machine.service;

import coffee_machine.model.entity.HistoryRecord;
import coffee_machine.model.entity.goods.Drink;

import java.util.List;

/**
 * Created by oleksij.onysymchuk@gmail on 15.11.2016.
 */
public interface CoffeeMachineService {

    HistoryRecord prepareDrinksForUser(List<Drink> drinks, int userId);

}
