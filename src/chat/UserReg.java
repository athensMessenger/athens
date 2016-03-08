package chat;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Zsolt Pazmandy
 * 1600690 / zxp590
 * University of Birmingham
 * Computer Science MSc 2015/16
 * on 26/02/16.
 * SWWS Group Project: Chat Application
 * User registration functions
 */
public class UserReg {

    private String username;
    private String password;
    private Scanner CSDB;
    private ArrayList<String> CSusers = new ArrayList<>();
    private boolean isVerified = false;
    private String nickname;
    private Connection connection;

    /**
     * Constructor reads the CSDB file into an ArrayList.
     * The CSDB file contains all CS usernames.
     */
    public UserReg() {
        try {
            CSDB = new Scanner(new FileReader("/home/zsolt/Dropbox/CS MSC/Project/src/chat/CSDB"));
            while (CSDB.hasNext()) {
                CSusers.add(CSDB.nextLine());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // Setting the Nimbus as default LAF
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

    }

    /**
     * getter for username
     *
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * setter for username
     *
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * getter for password
     * THIS FUNCTION AND THE PASSWORD VARIABLE ARE ONLY TEMPORARY
     * UNTIL WE INCORPORATE ENCRYPTION
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * setter for password
     * THIS FUNCTION AND THE PASSWORD VARIABLE ARE ONLY TEMPORARY
     * UNTIL WE INCORPORATE ENCRYPTION
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * getter for nickname
     *
     * @return nickname
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * setter for nickname
     *
     * @param nickname
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * getter for the active connection
     *
     * @return
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * setter for the active connection
     *
     * @param connection
     */
    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    /**
     * Sets up a connection to the database using Zsolt's defaults
     * (no separate .properties file implemented yet, please
     * change locally if it doesn't work)
     * @throws SQLException
     */
    public void setUpDBConnection() {
        try {
            this.connection = DriverManager.getConnection(
                    "jdbc:postgresql://dbteach2.cs.bham.ac.uk/athens_messenger",
                    "zxp590", "pizza");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Connected to database.");
    }

    /**
     * Checks whether given username exists in the UoB SoCS database.
     * If the username exists in the CS database, verifyRegistration()
     * is called.
     * If the username does not exist in the CS database, the
     * usernameDenied() function is called.
     *
     * @param username
     */
    public void verifyUsername(String username) {
        this.username = username;
        for (int i = 0; i < CSusers.size(); i++) {
            if (CSusers.get(i).equals(username)) {
                this.isVerified = true;
                System.out.println("Valid CS username.");
                verifyRegistration();
            }
        }
        if (!isVerified) usernameDenied();
    }

    /**
     * Verification of registration begins by establishing a connection
     * to the database (i.e. by calling the setUpConnection() method).
     *
     * An SQL query is sent to check whether the username the user has
     * entered exists in the userbase. If it DOES, the connection
     * is immediately closed & the registrationDenied() function is called.
     *
     * If the username has not been registered yet, or in case the
     * userbase table is empty, the enterNickname() function is called.
     */
    public void verifyRegistration() {

        setUpDBConnection();

        try {

            PreparedStatement query = getConnection().prepareStatement(
                    "SELECT id FROM userbase WHERE id = ?");
            query.setString(1, getUsername());
            ResultSet results = query.executeQuery();

            System.out.println("Checking if username has been registered previously...");

            if(results.next()) { // if the userbase table isn't empty

                // checking if the first column in the userbase table (id)
                // matches with the username selected (i.e. if it's been registered already or not)
                if (results.getString(1).equalsIgnoreCase(getUsername())) {
                    getConnection().close();
                    System.out.println("User registered alreddy.");
                    registrationDenied();
                } else {
                    enterNickname();
                }
            } else enterNickname(); // if this is the first user, the registration is verified

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Dialog box to inform the user that the username they're trying to register has
     * already been registered.
     */
    public void registrationDenied() {

        JFrame denied = new JFrame();
        denied.setSize(600, 100);
        denied.setVisible(true);
        denied.setLocation(300, 300);
        denied.setTitle("Registration failed!");
        denied.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        JLabel message = new JLabel("Username already registered. Registration failed. Forgot your password?");

        JButton OK = new JButton("OK");
        OK.addActionListener(ok -> {
            denied.dispose();
        });

        panel.add(message, BorderLayout.NORTH);
        panel.add(OK, BorderLayout.SOUTH);
        denied.add(panel);
    }

    /**
     * Dialog box to inform user that the username they're trying to register is not a valid CS
     * username.
     */
    public void usernameDenied() {
        JFrame denied = new JFrame();
        denied.setSize(500, 100);
        denied.setVisible(true);
        denied.setLocation(300, 300);
        denied.setTitle("Registration failed!");
        denied.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JLabel message = new JLabel("Username does not exist in CS database. Registration failed.");

        JButton OK = new JButton("OK");
        OK.addActionListener(ok -> {
            denied.dispose();
        });

        panel.add(message, BorderLayout.NORTH);
        panel.add(OK, BorderLayout.SOUTH);
        denied.add(panel);
    }

    /**
     * Records user-defined nickname, ensures the use of legal
     * characters.
     * RULES:   - 10 characters max
     *          - lowecase & uppercase allowed
     *          - numbers allowed
     *          - delimiters allowed: - _ .
     */
    public void enterNickname() {
        if (this.isVerified) {
            Scanner userInput = new Scanner(System.in);
            boolean isValidUserName = false;
            while (!isValidUserName) {
                System.out.println("Please enter your nickname (10 characters max): ");
                String nickname = userInput.nextLine();
                if (!nickname.matches("^[-\\w.]+$")) {
                    System.out.println("Please use a-z, A-Z, 0-9 only. Delimiters allowed (-_.)");
                } else if (nickname.length() > 10) {
                    System.out.println("Nickname too long, please use up to 8 characters");
                } else {
                    setNickname(nickname);
                    System.out.println("Your nickname is: " + getNickname());
                    enterPassword();
                    try {
                        PreparedStatement query = connection.prepareStatement(
                                "INSERT INTO userbase VALUES(?,?,?)");
                        query.setString(1, getUsername());
                        query.setString(2, getNickname());
                        query.setString(3, "datPassword");
                        query.execute();
                        System.out.println("Nickame saved in database.");

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    isValidUserName = true;
                }
            }
        }
    }

    /**
     * Records user-defined password, ensures the use of legal
     * characters (8-character alphabet-only)
     * *** RESTRICTIONS TO BE DEFINED ***
     * *** to be combined with ChatroomSecurity encryption functions
     * *** passwords will not be stored anywhere, only decryption keys
     */
    public void enterPassword() {
        Scanner userInput = new Scanner(System.in);
        boolean isValidUserName = false;
        while (!isValidUserName) {
            System.out.println("Please enter your password (8 characters max): ");
            String password = userInput.nextLine();
            if (!password.matches("[a-z]*")) {
                System.out.println("Please use letters ONLY. (a-z/A-Z)");
            } else if (password.length() > 8) {
                System.out.println("password too long, please use up to 8 characters");
            } else {
                setPassword(password);
                System.out.println("Your password has been registered");
                isValidUserName = true;
            }
        }
        userInput.close();
    }

    public static void main(String[] args) {
        // constructor creates generates acceptable usernames
        UserReg registration = new UserReg();

        // initiates the registration process with Zsolt's username
        registration.verifyUsername(args[0]);
    }
}

