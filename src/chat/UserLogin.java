package chat;

/**
 * Created by Zsolt Pazmandy
 * 1600690 / zxp590
 * University of Birmingham
 * School of Computer Science
 * MSc 2015/16
 * on 06/03/16.
 */
public class UserLogin {

    private static String username;
    private static String password;

    UserLogin(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void login(String username, String password) {
        Client c = new Client(username);
        c.login(username, password);
    }
}
