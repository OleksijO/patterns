package training.Facade;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oleksij.onysymchuk@gmail on 26.11.2016.
 */
public class FacadePatternUsing {
    CoffeeMachineService coffeeMachineService = CoffeeMachineServiceImpl.getInstance();
    // CoffeeMachineService interface implements the facade pattern for dao layer
    // all heavy logic (checks, withdrows etc.) is behind implementation of this interface

    public static void main(String[] args) {
        // it does not starts because classes simply copied (also not all needed) from web project 4.
        // classes placed only for example.

        int userId = getUserIdFromRequest(request);
        List<Drink> drinksToBuy = new ArrayList<>();

        // fill the list with drinks

        // try to buy
        try {
           prepareDrinksForUser(drinksToBuy, userId);
            System.out.println("drinks prepared");
        } catch (ApplicationException e){
            System.out.println("drinks did not prepared, because of "+ e.getMessage());
        }

    }

}
