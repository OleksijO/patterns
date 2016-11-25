package training.Command.command.user;

import coffee_machine.controller.Command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static coffee_machine.controller.Attributes.USER_ID;
import static coffee_machine.controller.PagesPaths.REDIRECTED;
import static coffee_machine.controller.PagesPaths.USER_HOME_PATH;
import static coffee_machine.controller.Parameters.LOGIN;
import static coffee_machine.controller.Parameters.PASSWORD;

public class UserLoginSubmitCommand implements Command {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException {
			String email = request.getParameter(LOGIN);
			String password = request.getParameter(PASSWORD);
			request.getSession().setAttribute(USER_ID, 1);
			// TODO
		    response.sendRedirect(USER_HOME_PATH);
		return REDIRECTED;
	}

}
