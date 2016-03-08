package chat;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by Zsolt Pazmandy
 * 1600690 / zxp590
 * University of Birmingham
 * Computer Science MSc 2015/16
 * on 04/03/16.
 */
public class Client extends Thread implements ClientImpl {

    // Network setup
    private Socket clientSocket;
    private String host = "tinky-winky.cs.bham.ac.uk";
    private int port = 50005;

    // I/O variables
    private Scanner response;
    private DataOutputStream request;
    private BufferedReader userInput;

    // Client variables
    private String username;
    private String nickname;
    private String password;
    private String message;
    private String pw1;
    private String pw2;
    private String username2;
    private String status;

    /**
     * REQUEST PROTOCOL DEFINITIONS
     * Each request stream from Client to Server begins
     * with a one-char-long identifier, so the server
     * can understand what kind of request is being sent
     */
    private final String loginProtocol = "1";
    private final String logoffProtocol = "0";
    private final String sendMessageProtocol = "2";
    private final String changePwProtocol = "3";
    private final String viewLogsProtocol = "4";
    private final String changeStatusProtocol = "5";


    /**
     * Constructor creates socket & initialises streams
     */
    public Client(String username) {
        this.username = username;
        try {

            clientSocket = new Socket(host, port);
            response = new Scanner(clientSocket.getInputStream());
            request = new DataOutputStream(clientSocket.getOutputStream());
            userInput = new BufferedReader(new InputStreamReader(System.in));


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public String getPassword() {
        return password;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPw1() {
        return pw1;
    }

    public String getPw2() {
        return pw2;
    }

    public String getUsername2() {
        return username2;
    }

    public String getStatus() {
        return status;
    }




    @Override
    public void login(String username, String password) {

        try {
            request.writeBytes(loginProtocol + getUsername() + getPassword());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void logoff(String username, String password) {
        this.username = username;
        try {
            request.writeBytes(logoffProtocol + getUsername());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendMessage() {
        try {
            setMessage(userInput.readLine());
            request.writeBytes(sendMessageProtocol + getUsername() + getMessage() + '\n');
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void changePw() {
        try {
            request.writeBytes(changePwProtocol + getUsername() + getPw1() + getPw2());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void viewLogs() {
        try {
            request.writeBytes(viewLogsProtocol + getUsername() + getUsername2());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void changeStatus(String status) {
        try {
            request.writeBytes(changeStatusProtocol + getUsername() + status);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
