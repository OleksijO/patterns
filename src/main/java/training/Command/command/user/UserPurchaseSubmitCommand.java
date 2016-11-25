package training.Command.command.user;

import coffee_machine.controller.Attributes;
import coffee_machine.controller.Command;
import coffee_machine.controller.PagesPaths;
import coffee_machine.controller.RegExp;
import coffee_machine.i18n.message.key.General;
import coffee_machine.model.entity.HistoryRecord;
import coffee_machine.model.entity.goods.Drink;
import coffee_machine.service.AccountService;
import coffee_machine.service.CoffeeMachineService;
import coffee_machine.service.DrinkService;
import coffee_machine.service.exception.ServiceException;
import coffee_machine.service.impl.AccountServiceImpl;
import coffee_machine.service.impl.CoffeeMachineServiceImpl;
import coffee_machine.service.impl.DrinkServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static coffee_machine.controller.Attributes.*;

public class UserPurchaseSubmitCommand implements Command {
	private DrinkService drinkService = DrinkServiceImpl.getInstance();
	private AccountService accountService = AccountServiceImpl.getInstance();
	private CoffeeMachineService coffeeMachine = CoffeeMachineServiceImpl.getInstance();
	private Pattern patternNumber = Pattern.compile(RegExp.REGEXP_NUMBER);
	private Pattern patternDrink = Pattern.compile(RegExp.REGEXP_DRINK_PARAM);
	private Pattern patternAddonInDrink = Pattern.compile(RegExp.REGEXP_ADDON_IN_DRINK_PARAM);

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		request.setAttribute(Attributes.PAGE_TITLE, General.TITLE_USER_PURCHASE);
		try {
			List<Drink> drinksToBuy = getDrinksFromRequest(request);
			int userId = (int) request.getSession().getAttribute(USER_ID);
			HistoryRecord record = coffeeMachine.prepareDrinksForUser(drinksToBuy, userId);
			request.setAttribute(USER_BALANCE, accountService.getByUserId(userId).getRealAmount());
			request.setAttribute(USUAL_MESSAGE, General.PURCHASE_THANKS_MESSAGE);
			request.setAttribute(USUAL_ADDITIONAL_MESSAGE, record.toString());
		} catch (ServiceException e) {
			request.setAttribute(ERROR_MESSAGE, General.ERROR_PURCHASE_DRINKS);
			// TODO remove hardcode
			request.setAttribute(ERROR_ADDITIONAL_MESSAGE, "error.user.purchase.unknown");
			// request.setAttribute(ERROR_ADDITIONAL_MESSAGE, e.getMessage()); /
		}

		return PagesPaths.USER_PURCHASE_PAGE;
	}

	private List<Drink> getDrinksFromRequest(HttpServletRequest request) {
		Enumeration<String> params = request.getParameterNames();
		Map<Integer, Integer> drinkQuantityByIds = new HashMap<>();
		while (params.hasMoreElements()) {
			String param = params.nextElement();
			Matcher matcher = patternDrink.matcher(param);
			if (matcher.matches()) {
				int drinkQuantity = getIntFromRequestByParameter(param, request);
				if (drinkQuantity > 0) {
					int drinkId = getDrinkIdFromParam(param);
					drinkQuantityByIds.put(drinkId, drinkQuantity);
				}
			}
		}
		List<Drink> drinks = drinkService.getAllByIdSet(drinkQuantityByIds.keySet());
		drinks = getBaseDrinkListAndSetDrinkQuantities(drinks, drinkQuantityByIds);
		params = request.getParameterNames();
		while (params.hasMoreElements()) {
			String param = params.nextElement();
			Matcher matcher = patternAddonInDrink.matcher(param);
			if (matcher.matches()) {
				int addonQuantity = getIntFromRequestByParameter(param, request);
				if (addonQuantity > 0) {
					int drinkId = getDrinkIdFromParam(param);
					addAddonToDrinkInList(drinkId, getAddonIdFromParam(param), addonQuantity, drinks);
				}
			}
		}
		return drinks;
	}

	private int getIntFromRequestByParameter(String param, HttpServletRequest request) {
		try {
			return Integer.parseInt(request.getParameter(param));
		} catch (Exception e) {
			throw new ServiceException("Error while getting int from request");
		}

	}

	private int getDrinkIdFromParam(String param) {
		Matcher matcher = patternNumber.matcher(param);
		if (matcher.find(0)) {
			return Integer.parseInt(param.substring(matcher.start(), matcher.end()));
		} else {
			throw new ServiceException("Here in param must be an id!");
		}
	}

	private List<Drink> getBaseDrinkListAndSetDrinkQuantities(List<Drink> drinks,
			Map<Integer, Integer> drinkQuantityByIds) {
		List<Drink> baseDrinks = new ArrayList<>();
		drinks.forEach(drink -> {
			if (drinkQuantityByIds.containsKey(drink.getId())) {
				Drink baseDrink = drink.getBaseDrink();
				baseDrink.setQuantity(drinkQuantityByIds.get(drink.getId()));
				baseDrinks.add(baseDrink);
			} else {
				throw new ServiceException("It can not be! Drink was in list of purchase and it should be in DB!");
			}
		});
		return drinks;

	}

	private int getAddonIdFromParam(String param) {
		Matcher matcher = patternNumber.matcher(param);
		matcher.find(0);
		if (matcher.find(matcher.end())) {
			return Integer.parseInt(param.substring(matcher.start(), matcher.end()));
		} else {
			throw new ServiceException("Here in param must be an id!");
		}
	}

	private void addAddonToDrinkInList(int drinkId, int addonId, int addonQuantity, List<Drink> drinks) {

		drinks.forEach(drink -> {
			if (drink.getId() == drinkId) {
				drink.getAddons().forEach(addon -> {
					if (addon.getId() == addonId) {
						addon.setQuantity(addon.getQuantity() + addonQuantity);
					}
				});
			}
		});

	}

}
