package chat;

import java.util.Scanner;

/**
 * Created by Zsolt Pazmandy
 * 1600690 / zxp590
 * University of Birmingham
 * School of Computer Science
 * MSc 2015/16
 * on 06/03/16.
 */
public class Start {

    public static void main(String[] args) {

        Scanner pick = new Scanner(System.in);
        Scanner userInput = new Scanner(System.in);
        int choice = 0;
        String username;
        String password;

        System.out.println("Enter '1' to login.");
        System.out.println("Enter '2' to register.");
        System.out.println("Enter '3' if you've forgotten your password.");
        System.out.print("Selection: ");

        choice = pick.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("\nPlease enter your CS username: ");
                    username = userInput.nextLine();

                    System.out.print("\nPlease enter your Chat password: ");
                    password = userInput.nextLine();
                    UserLogin u = new UserLogin(username, password);
                    u.login(username, password);
                    break;
                case 2:
                    System.out.print("Please enter your CS username: ");
                    username = userInput.nextLine();

                    UserReg reg = new UserReg();
                    reg.verifyUsername(username);
                    break;
                case 3:
                    // ForgottenPassword.main();
                    break;
            }

    }
}
