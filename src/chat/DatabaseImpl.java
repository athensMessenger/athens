package chat;

import java.sql.Time;

/**
 * Created by Zsolt Pazmandy
 * 1600690 / zxp590
 * University of Birmingham
 * Computer Science MSc 2015/16
 * on 04/03/16.
 */
public interface DatabaseImpl {
    public String getNickname(String username);
    public String getPwHash(String username);
    public String getLog(String username, Time time);
}
