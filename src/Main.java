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
                         "\33[1;30m=====================================\033[0m" +
                         "\033[0;36mWould you like to:\n1. Login\n2. Exit the program");
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

    public static void loggedMenu() {

    }

    //method will clear the console whenever called
    public static void flush() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    //method causes the program to pause for finite determined time period; usually to show errors and then clear them to reduce clutter
    public static void sleep(int milliseconds) throws InterruptedException {
        Thread.sleep(milliseconds);
    }



}