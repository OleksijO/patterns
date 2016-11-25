package training.Command.command.user;

import coffee_machine.controller.Attributes;
import coffee_machine.controller.Command;
import coffee_machine.i18n.message.key.General;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static coffee_machine.controller.PagesPaths.USER_HOME_PAGE;

public class UserHomeCommand implements Command {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		request.setAttribute(Attributes.PAGE_TITLE, General.TITLE_USER_HOME);
		return USER_HOME_PAGE;
	}

}
