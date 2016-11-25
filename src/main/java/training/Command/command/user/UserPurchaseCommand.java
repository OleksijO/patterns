package training.Command.command.user;

import coffee_machine.controller.Attributes;
import coffee_machine.controller.Command;
import coffee_machine.i18n.message.key.General;
import coffee_machine.service.AccountService;
import coffee_machine.service.AddonService;
import coffee_machine.service.DrinkService;
import coffee_machine.service.impl.AccountServiceImpl;
import coffee_machine.service.impl.AddonServiceImpl;
import coffee_machine.service.impl.DrinkServiceImpl;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static coffee_machine.controller.Attributes.*;
import static coffee_machine.controller.PagesPaths.USER_PURCHASE_PAGE;

public class UserPurchaseCommand implements Command {
	private static final Logger logger = Logger.getLogger(UserPurchaseCommand.class);
	private DrinkService drinkService = DrinkServiceImpl.getInstance();
	private AddonService addonService = AddonServiceImpl.getInstance();
	private AccountService accountService = AccountServiceImpl.getInstance();

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {

		int userId = (int) request.getSession().getAttribute(USER_ID);
		request.setAttribute(Attributes.PAGE_TITLE, General.TITLE_USER_PURCHASE);
		request.setAttribute(USER_BALANCE,
		accountService.getByUserId(userId).getRealAmount());
		request.setAttribute(REFILL_DRINKS, drinkService.getAll());
		request.setAttribute(REFILL_ADDONS, addonService.getAll());

		return USER_PURCHASE_PAGE;
	}

}
