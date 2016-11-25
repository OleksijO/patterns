package training.Command;

import coffee_machine.controller.impl.CommandHolderImpl;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static coffee_machine.controller.PagesPaths.*;

/**
 * Servlet implementation class MainController
 */
// @WebServlet("/*")
public class MainController {
    private CommandHolder commandHolder;


    public void init() throws ServletException {
        commandHolder = new CommandHolderImpl();
        commandHolder.init();
    }

    private void processRequest(Command command, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        if (command == null) {
            response.sendRedirect(HOME_PATH);
            return;
        }

        String view = command.execute(request, response);

        request.getRequestDispatcher(view).forward(request, response);
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(commandHolder.get(request.getRequestURI()), request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(commandHolder.post(request.getRequestURI()), request, response);
    }

}
