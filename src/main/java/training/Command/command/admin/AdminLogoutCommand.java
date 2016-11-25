package training.Command.command.admin;

import coffee_machine.controller.Command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static coffee_machine.controller.Attributes.ADMIN_ID;
import static coffee_machine.controller.PagesPaths.HOME_PATH;
import static coffee_machine.controller.PagesPaths.REDIRECTED;

public class AdminLogoutCommand implements Command {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException {

		request.getSession().removeAttribute(ADMIN_ID);

		response.sendRedirect(HOME_PATH);

		return REDIRECTED;
	}

}
