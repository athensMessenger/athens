package chat;

/**
 * Created by Zsolt Pazmandy
 * 1600690 / zxp590
 * University of Birmingham
 * Computer Science MSc 2015/16
 * on 04/03/16.
 */
public interface ChatroomEncryption {
    public String encrypt(String message, String key);
    public String decrypt(String message, String key);
}
