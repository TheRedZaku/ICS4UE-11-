import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    //colour codes:
    //error codes in red
    public static final String RED_BOLD_BRIGHT = "\033[1;91m"; //Bold High Intensity
    //system in green, purple or blue
    public static final String GREEN_UNDERLINED = "\033[4;32m";  //Underline
    public static final String PURPLE = "\033[0;35m";  // Normal
    public static final String CYAN = "\033[0;36m";    // Normal
    //green is usually validation
    //plain white can also be used
    //default to revert to normal colour text
    public static final String RESET = "\033[0m";  // Text Reset

    public static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) throws InterruptedException {
        //Basic code layout for the program
        //semi pseudo-code and actual programming


        mainMenu();
    }

    //this will act as a log in screen for the user
    public static void mainMenu() throws InterruptedException {
        //implement a try catch login
        do {
             try {
                 flush(); //initial clear means we can avoid having to redundantly call the clear later down the line
                 System.out.println("\033[1;36mEncryption Program" +
                         "\33[1;30m\n=====================================\033[0m" +
                         "\033[0;36m\nWould you like to:\n1. Login\n2. Exit the program");
                switch(scanner.nextInt()) {
                    case 1 -> loggedMenu();//takes the user to the login menu, where they will proceed to log into or create a new account
                    case 2 -> {//tells the user that the program is exiting, and then clears and exits the program
                        System.out.println("\033[4;32mExiting...\033[0m");
                        sleep(1000);
                        flush();
                        System.exit(0);
                    }
                    default -> {
                        System.out.println(RED_BOLD_BRIGHT + "Option not found. Please try again." + RESET);
                        sleep(1000);
                    }
                }
             } catch (InputMismatchException ime) {
                 System.out.println(RED_BOLD_BRIGHT + "Input must be an Integer. Please try again." + RESET);
                 sleep(1000);
                 //end of loop will return to beginning
             }
        } while (true);
    }

    public static void loggedMenu() throws InterruptedException {
        //This is how we keep data separate and enable the user to traverse / log in and log out while also closing data.
        do {
            try {
                flush();
                System.out.println("\033[1:36mWelcome to the Login Menu\033[0m" +
                        " Would you like to:\n1. Log into an existing account\n2. Recover an account\n4. Return to main menu");
                switch (scanner.nextInt()) {
                    case 1 -> logIn();
                    case 2 -> createAccount();
                    case 3 -> recoverAccount();
                    case 4 -> mainMenu();
                    default -> {
                        System.out.println(RED_BOLD_BRIGHT + "Option not found. Please try again." + RESET);
                        sleep(1000);
                    }
                }
            } catch (InputMismatchException ime) {
                System.out.println(RED_BOLD_BRIGHT + "Input must be an Integer. Please try again." + RESET);
                sleep(1000);
                //end of loop will return to beginning
            }
        } while (true);
    }

    //method to allow the user to create a new account
    public static void createAccount() {
        //Pseudo code
        /*
        do try
        pull from existing file for pre-existing username and prevent duplicates
        ask for a username
        if the username exists, try again
        ensure password follows certain guidelines, such as length, use of capitals or symbols, etc...
        give the user a recovery code in case they forget account password
        clear the temp variable (automatically done when program exits)

        catch while true
         */
    }

    //method to allow a user to log into an account they just/already created
    public static void logIn() {
        //pseudo code
        /*
        do try
        again, call a method to scan for existing usernames, save to temporary
        ask for a user, if it matches -> search using sorted linear search in alphabetical list of users
        else continue
        ask for password, repeat same steps
        clear all data after, enter user account -> some other method
        catch while true
         */
    }

    //method to allow a user to recover their account
    public static void recoverAccount() {
        //Pseudo code
        /*
        do try
            ask for a username -> same steps as before, scan file, save to temporary, look for existing data
            if match, retry, else continue
            ask for their recovery code, match with before, ask to re-enter if they get it wrong
            if they do 3 tries wrong, kick them back to main menu
            if they do manage to get in, they are given the opportunity to reset their password, and their recovery code is changed to something else and they are shown the code only once
            otherwise they can view their recovery code in their account in some menu
        catch while true
         */
    }

    //method will clear the console whenever called
    public static void flush() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    //method causes the program to pause for finite determined time period; usually to show errors and then clear them to reduce clutter
    public static void sleep(int milliseconds) throws InterruptedException {
        Thread.sleep(milliseconds);
        //this causes an interrupted exception in the program as you interrupt an execution -> that is why throwing an InterruptedException is necessary so that the program knows that it's intentional
    }



}