//I hope no one has a problem with me continually passing between methods -> keeps most things isolated from each other and declutters a lot more
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class User {
    //colour codes:
    //error codes in red -> \033[1;91m -> red high contrast
    //system in green, purple or blue -> \033[0;32m, \033[0;35m, \033[0;34m
    //green is usually validation
    //plain white can also be used -> \033[0;37m
    //default to revert to normal colour text -> \033[0m
    public static BufferedWriter writer;
    public static BufferedReader reader;
    public static Scanner scanner = new Scanner(System.in);
    public static Scanner scInt = new Scanner(System.in);
    public static ArrayList<String> usernames = new ArrayList<>(); //list of all users.
    public static ArrayList<Storage> programUsers = new ArrayList<>(); // I feel like all the users should be within an object class
    //passwords haven't been added, because they would be saved in a text file with no encryption, unless the text file itself was encrypted server side
    public static void main(String[] args) throws InterruptedException {
        //Basic code layout for the program
        //semi pseudo-code and actual
        mainMenu();
    }

    //this will act as a log in screen for the user
    public static void mainMenu() throws InterruptedException {
        //implement a try catch login
        do {
            try {

                flush(); //initial clear means we can avoid having to redundantly call the clear later down the line
                readFromFile();
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
                        System.out.println("\033[1;91mOption not found. Please try again.\033[0m");
                        sleep(1000);
                    }
                }
            } catch (InputMismatchException ime) {
                System.out.println("\033[1;91mInput must be an Integer. Please try again.\033[0m");
                sleep(1000);
                //end of loop will return to beginning
            }
        } while (true);
    }

    //acts as the proxy for the user if they want to create or already have a user
    public static void loggedMenu() throws InterruptedException {
        //This is how we keep data separate and enable the user to traverse / log in and log out while also closing data.
        do {
            try {
                flush();
                System.out.println("\033[1:36mWelcome to the Login Menu\033[0m" +
                        " Would you like to:\n1. Log into an existing account\n2. Create an account\n3. Return to main menu");
                switch (scanner.nextInt()) {
                    case 1 -> logIn();
                    case 2 -> createAccount();
                    case 3 -> mainMenu();
                    default -> {
                        System.out.println("\033[1;91mOption not found. Please try again.\033[0m");
                        sleep(1000);
                    }
                }
            } catch (InputMismatchException ime) {
                System.out.println("\033[1;91mInput must be an Integer. Please try again.\033[0m");
                sleep(1000);
                //end of loop will return to beginning
            }
        } while (true);
    }

    //This method checks if the username that the user has entered already exists or not.
    public static boolean doesAccountExist(String newUser) {
        String newLine = "";
        boolean check = false;
            for (String userIndex: usernames) {//linear search algorithm
                if (userIndex.equalsIgnoreCase(newUser)) {//if time permitted, I would have done this as a binary search for Strings
                    check = true;
                }
            }
        return check;
    }

    //updates the list of usernames from the text file to the server
    public static void readFromFile() throws InterruptedException {
        String newLine = "";
        boolean check = false;
        try {
            reader = new BufferedReader(new FileReader("src/loginInfo.txt"));
            while ((newLine = reader.readLine()) != null) {//runs as long as the next line found is not empty
                programUsers.add(new Storage(newLine, 0));
                usernames.add(newLine);
//                System.out.println(usernames);
//                sleep(1000); debugging
            }//adds all items to the array; no need to check for validity as it should be done prior to adding the data.
            reader.close();
            Collections.sort(usernames);//sorts the arrayList in alphabetical ascending order
        } catch (IOException e) {
            e.printStackTrace();
        }

    } //method is only called once the program is run, it can be updated without having to call again

    //method to allow the user to create a new account
    public static void createAccount() throws InterruptedException {
        String newUser = "";
        do {
            try {
                flush();//clears screen before presenting options
                System.out.print("\033[1;36mWhat do you want your username to be [Between 8 - 20 characters;Not case sensitive]: \033[0m");
                newUser = scanner.nextLine();//sets a temporary variable to the userInput that they want as their username
                while (doesAccountExist(newUser) && newUser.length() > 7 && newUser.length() < 21) {//calls a method that reads from the txt file for an existing username
                    System.out.println("\033[1;91mUsername already exists or is too long/short. Please try again\033[0m");
                    newUser = scanner.nextLine();
                }
                usernames.add(newUser);
                System.out.println("\033[1;32mAccount created. Returning to main menu...\033[0m");
                sleep(1000);
                mainMenu();
            } catch (InputMismatchException ime) {
                System.out.println("\033[1;91mInput must be a String. Please try again.\033[0m");
                sleep(1000);
            }
        } while (true);
    }

    //method to allow a user to log into an account they just/already created
    public static void logIn() throws InterruptedException {
        String username = "";
        boolean matchFound = true;
        do {
            try {
                System.out.print("\033[1;36mWhat is your username: \033[0m");
                username = scanner.nextLine();
                for (String existingUser: usernames) {//checks if the username matches that of an existing one. If it does, it lets you log in
                    if (existingUser.equalsIgnoreCase(username)) {
                        System.out.println("\033[1;34mUser exists. Proceeding...\033[0m");
                        sleep(1000);
                        matchFound = true;
                        userDataMenu(username);
                    }
                }
                matchFound = false;
                if (!matchFound) {
                    System.out.println("\033[1;91mUser not found. Please try again.\033[0m");
                    sleep(1000);
                }
            } catch (InputMismatchException ime) {
                System.out.println("\033[1;91mInput must be a String. Please try again.\033[0m");
                sleep(1000);
            }
        } while (true);
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

//    //method to allow a user to recover their account
//    public static void recoverAccount() {
//        //Pseudo code
//        /*
//        do try
//            ask for a username -> same steps as before, scan file, save to temporary, look for existing data
//            if match, retry, else continue
//            ask for their recovery code, match with before, ask to re-enter if they get it wrong
//            if they do 3 tries wrong, kick them back to main menu
//            if they do manage to get in, they are given the opportunity to reset their password, and their recovery code is changed to something else and they are shown the code only once
//            otherwise they can view their recovery code in their account in some menu
//        catch while true
//         */
//    }

    public static void userDataMenu(String username) throws InterruptedException {
        System.out.println("\033[1;36mWould you like to:\n1. Access a previous message\n2. Write a new message\n3. Log out");
        switch (scInt.nextInt()) {
            case 1 -> oldMessageList(username);
            case 2 -> newMessage(username);
            case 3 -> {
                System.out.println("\033[1;34mReturning to main menu...\033[0m");
                sleep(1000);
                mainMenu();
            }
        }
    }

    public static void newMessage(String username) throws InterruptedException {
        int userIndex = 0;
        String message = "";
        Pattern pattern;
        Matcher matcher;
        boolean matchFound;
        do {
            try {
                //using the pattern and matcher built in methods, the program looks for matches using regex for characters that are not String literals (i.e. numbers or symbols)
                System.out.print("\033[1;36mWhat message would you like to save [Cannot contains symbols or numbers]: \033[0m");
                message = scanner.nextLine();
                pattern = Pattern.compile(message, Pattern.CASE_INSENSITIVE);
                matcher = pattern.matcher("[^\\s^a-z]"); //sets the matcher to find any input that is not char a to z and not a whitespace (i.e. a number or symbol)
                matchFound = matcher.find(); //looks for a match
                if (matchFound) {
                    System.out.println("\033[1;91mInput cannot contain symbols or numbers. Please try again.\033[0m");
                    sleep(1000);
                } else {
                    for (int index = 0; index < usernames.size(); index++) {//loop to grab the index of the user currently being accessed. This could also be put into a separate method
                        if (username.equalsIgnoreCase(usernames.get(index))) {
                            userIndex = index;//save the index that the current username is at
                        }
                    }
                    programUsers.get(userIndex).setUserInfo(message);//bro I'm not quite sure I remember how to add a new line to an array list within an object
                }
            } catch (InputMismatchException ime) {
                System.out.println("\033[1;91mInput must be a String. Please try again.\033[0m");
                sleep(1000);
            }
        } while (true);

    }

    public static void oldMessageList(String username) throws InterruptedException{

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