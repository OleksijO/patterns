package training;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by oleksij.onysymchuk@gmail on 26.11.2016.
 */
public class MediatorPattern {
    public static void main(String[] args) {
        ChatServer server = new ChatServerImpl();
        User user1 = new UserImpl("user1");
        User user2 = new UserImpl("user2");
        User user3 = new UserImpl("user3");
        User user4 = new UserImpl("user4");
        User user5 = new UserImpl("user5");

        user1.loginToServer(server);
        user2.loginToServer(server);
        user3.loginToServer(server);
        user3.send("Hello everybody!");
        user2.send("Hello!");
        user1.send("Hi!");
        user4.loginToServer(server);
        user5.loginToServer(server);
        user5.logout();
        user1.send(" Goodbye!");
        user1.logout();
        user2.sendDirectMessage("How are you?", user3);
        user3.sendDirectMessage("I'm OK, and you?", user2);
        user2.logout();
        user3.logout();
        user4.logout();

    }
    interface ChatServer{
        List<User> getUsers();
        void sendMessageToAll(String message, User me);
        void sendMessageToUser(String message, User me, User user);
        void loginMe(User user);
        void logoutMe(User user);
    }

    interface User{
        void notify(String message);
        void loginToServer(ChatServer chatServer);
        String getName();
        void send (String message);
        void sendDirectMessage(String message, User user);
        void logout();
    }

    static class ChatServerImpl implements ChatServer{
        private List<User> users = new LinkedList<>();
        private User chatBot = new UserImpl("ChatBot");

        public List<User> getUsers() {
            return users;
        }

        public void sendMessageToAll(String message, User me) {
            users.forEach(user -> user.notify(me.getName()+" : "+message));
        }

        public void sendMessageToUser(String message, User me, User user) {
           if (user!=null) {
               user.notify("[Private] "+me.getName()+" : "+message);
           }
        }

        public void loginMe(User user) {
           if (user!=null) {
               users.add(user);
               sendMessageToAll("User "+user.getName()+" connected to the chat.", chatBot);
           }
        }

        public void logoutMe(User user) {
            if (user!=null){
                users.remove(user);
                sendMessageToAll("User "+user.getName()+" left the chat.", chatBot);
            }
        }
    }

    static class UserImpl implements User{
        private String name;
        private ChatServer server;

        public UserImpl(String name) {
            this.name = name;
        }

        public void notify(String message) {
            System.out.println("User ["+name+"] got message: "+message);
        }

        public void loginToServer(ChatServer chatServer) {
            server = chatServer;
            server.loginMe(this);
        }

        public String getName() {
           return name;
        }

        public void send(String message){
            server.sendMessageToAll(message, this);
        }

        public void sendDirectMessage(String message, User user){
            server.sendMessageToUser(message,this, user);
        }

        public void logout(){
            server.logoutMe(this);
        }
    }
}
