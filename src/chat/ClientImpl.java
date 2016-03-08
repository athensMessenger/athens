package chat;

/**
 * Created by Zsolt Pazmandy
 * 1600690 / zxp590
 * University of Birmingham
 * Computer Science MSc 2015/16
 * on 04/03/16.
 */
public interface ClientImpl {
    public void sendMessage();
    public void login(String username, String password);
    public void logoff(String username, String password);
    public void changePw();
    public void viewLogs();
    public void changeStatus(String status);
}
