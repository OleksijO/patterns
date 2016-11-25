package training.Command.command.admin;

import coffee_machine.controller.Attributes;
import coffee_machine.controller.Command;
import coffee_machine.i18n.message.key.General;
import coffee_machine.service.AddonService;
import coffee_machine.service.DrinkService;
import coffee_machine.service.impl.AddonServiceImpl;
import coffee_machine.service.impl.DrinkServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static coffee_machine.controller.Attributes.REFILL_ADDONS;
import static coffee_machine.controller.Attributes.REFILL_DRINKS;
import static coffee_machine.controller.PagesPaths.ADMIN_REFILL_PAGE;

public class AdminRefillSubmitCommand implements Command {
	private DrinkService drinkService = DrinkServiceImpl.getInstance();
	private AddonService addonService = AddonServiceImpl.getInstance();

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {

		// TODO refill logic

		request.setAttribute(REFILL_DRINKS, drinkService.getAll());
		request.setAttribute(REFILL_ADDONS, addonService.getAll());
		request.setAttribute(Attributes.PAGE_TITLE, General.TITLE_ADMIN_REFILL);
		return ADMIN_REFILL_PAGE;
	}

}
