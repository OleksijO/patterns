package coffee_machine.controller.impl;

import coffee_machine.controller.Command;
import coffee_machine.controller.CommandHolder;
import coffee_machine.controller.impl.command.HomeCommand;
import coffee_machine.controller.impl.command.admin.*;
import coffee_machine.controller.impl.command.user.*;

import java.util.HashMap;
import java.util.Map;

import static coffee_machine.controller.PagesPaths.*;

/**
 * Created by oleksij.onysymchuk@gmail on 17.11.2016.
 */
public class CommandHolderImpl implements CommandHolder {
    private Map<String, Command> getCommands = new HashMap<>();
    private Map<String, Command> postCommands = new HashMap<>();

    @Override
    public Command get(String path) {
        return getCommands.get(path);
    }

    @Override
    public Command post(String path) {
        return postCommands.get(path);
    }

    @Override
    public void init(){
        getCommands.put(HOME_PATH, new HomeCommand());

        getCommands.put(USER_LOGIN_PATH, new UserLoginCommand());
        getCommands.put(USER_LOGOUT_PATH, new UserLogoutCommand());
        getCommands.put(USER_HOME_PATH, new UserHomeCommand());
        getCommands.put(USER_PURCHASE_PATH, new UserPurchaseCommand());
        getCommands.put(USER_HISTORY_PATH, new UserHistoryCommand());

        getCommands.put(ADMIN_LOGIN_PATH, new AdminLoginCommand());
        getCommands.put(ADMIN_LOGOUT_PATH, new AdminLogoutCommand());
        getCommands.put(ADMIN_REFILL_PATH, new AdminRefillCommand());
        getCommands.put(ADMIN_HOME_PATH, new AdminHomeCommand());

        postCommands.put(USER_LOGIN_PATH, new UserLoginSubmitCommand());
        postCommands.put(USER_PURCHASE_PATH, new UserPurchaseSubmitCommand());

        postCommands.put(ADMIN_REFILL_PATH, new AdminRefillSubmitCommand());
        postCommands.put(ADMIN_LOGIN_PATH, new AdminLoginSubmitCommand());

    }
}
