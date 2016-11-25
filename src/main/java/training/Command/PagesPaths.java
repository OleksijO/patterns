package training.Command;

public final class PagesPaths {
	public static final String JSP = ".jsp";
	public static final String VIEW_CLASSPATH = "/WEB-INF/view";
	public static final String USER = "/user";
	public static final String ADMIN = "/admin";
	public static final String LOGIN = "/login";
	public static final String LOGOUT = "/logout";
	public static final String HOME_PATH = "/home";
	public static final String HISTORY = "/history";
	public static final String PURCHASE = "/purchase";
	public static final String REFILL = "/refill";
	public static final String REDIRECTED = "REDIRECTED";
	public static final String USER_LOGIN_PATH = USER + LOGIN;
	public static final String USER_LOGOUT_PATH = USER + LOGOUT;
	public static final String USER_HISTORY_PATH = USER + HISTORY;

	public static final String ADMIN_LOGIN_PATH = ADMIN + LOGIN;
	public static final String ADMIN_LOGOUT_PATH = ADMIN + LOGOUT;
	public static final String PAGE = "Page" + JSP;
	public static final String HOME_PAGE = VIEW_CLASSPATH + HOME_PATH + PAGE;
	public static final String USER_HOME_PATH = USER + HOME_PATH;
	public static final String USER_PURCHASE_PATH = USER + PURCHASE;
	public static final String USER_PURCHASE_PAGE = VIEW_CLASSPATH + USER + PURCHASE + PAGE;
	public static final String USER_HOME_PAGE = VIEW_CLASSPATH + USER_HOME_PATH + PAGE;
	public static final String ADMIN_HOME_PATH = ADMIN + HOME_PATH;
	public static final String ADMIN_HOME_PAGE = VIEW_CLASSPATH + ADMIN_HOME_PATH + PAGE;
	public static final String USER_HISTORY_PAGE = VIEW_CLASSPATH + USER + HISTORY + PAGE;
	public static final String USER_LOGIN_PAGE = VIEW_CLASSPATH + USER_LOGIN_PATH + PAGE;
	public static final String ADMIN_LOGIN_PAGE = VIEW_CLASSPATH + ADMIN_LOGIN_PATH + PAGE;

	public static final String ADMIN_REFILL_PATH = ADMIN + REFILL;
	public static final String ADMIN_REFILL_PAGE = VIEW_CLASSPATH + ADMIN_REFILL_PATH + PAGE;
	public static final String HEADER = VIEW_CLASSPATH + "/fragment/header" + JSP;
	public static final String FOOTER = VIEW_CLASSPATH + "/fragment/footer" + JSP;
	public static final String SOURCES_PATH = "https://github.com/OleksijO/coffee-machine";

}
