package chat;

/**
 * Created by Zsolt Pazmandy
 * 1600690 / zxp590
 * University of Birmingham
 * Computer Science MSc 2015/16
 * on 04/03/16.
 */
public interface ServerImpl {
    public void updateUserList();
    public void notifyClient(String name);
}
