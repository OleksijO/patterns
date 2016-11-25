package training.Command.command;

import coffee_machine.controller.Attributes;
import coffee_machine.controller.Command;
import coffee_machine.i18n.message.key.General;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static coffee_machine.controller.Attributes.ERROR_MESSAGE;
import static coffee_machine.controller.Attributes.USUAL_MESSAGE;
import static coffee_machine.controller.PagesPaths.HOME_PAGE;
import static coffee_machine.i18n.message.key.General.TEST_ERROR_MESSAGE;
import static coffee_machine.i18n.message.key.General.TEST_USUAL_MESSAGE;

public class HomeCommand implements Command {
	private static final Logger logger = Logger.getLogger(HomeCommand.class);
	private static int counter = 0; // TODO DELETE AFTER TESTING

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		// TODO DELETE HARDCODE AFTER TESTS

		if (counter % 2 == 0) {
			request.setAttribute(USUAL_MESSAGE, TEST_USUAL_MESSAGE);
		}
		if (counter % 3 == 0) {
			request.setAttribute(ERROR_MESSAGE, TEST_ERROR_MESSAGE);
		}
		counter++;
		// TODO end of hardcode
		request.setAttribute(Attributes.PAGE_TITLE, General.TITLE_HOME);
		return HOME_PAGE;
	}

}
