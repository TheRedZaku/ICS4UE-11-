//I hope no one has a problem with me continually passing between methods -> keeps most things isolated from each other and declutters a lot more
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
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
    public static Scanner scanner2 = new Scanner(System.in);
    public static Scanner scInt = new Scanner(System.in);
    public static ArrayList<String> usernames = new ArrayList<>(); //list of all users.
    public static ArrayList<Storage> programUsers = new ArrayList<>(); // I feel like all the users should be within an object class
    public static ArrayList<CaesarCipher> caesarCiphers = new ArrayList<>();
    public static ArrayList<AtbashCipher> atbashCiphers = new ArrayList<>();
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
            usernames.clear();
            programUsers.clear();
            try {
                flush(); //initial clear means we can avoid having to redundantly call the clear later down the line
                readFromFileUsers();
                readFromFileMessageData();
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
                    check = true;//returns true if the account username already exists
                }
            }
        return check;
    }

    //updates the list of usernames from the text file to the server
    public static void readFromFileUsers() throws InterruptedException {
        String newLine;
        try {
            reader = new BufferedReader(new FileReader("src/loginInfo.txt"));
            while ((newLine = reader.readLine()) != null) {//runs as long as the next line found is not empty
                usernames.add(newLine);
            }//adds all items to the array; no need to check for validity as it should be done prior to adding the data.
            reader.close();
//            Collections.sort(usernames);//sorts the arrayList in alphabetical ascending order
        } catch (IOException e) {
            e.printStackTrace();
        }

    } //method is only called once the program is run, it can be updated without having to call again

    public static void readFromFileMessageData() throws InterruptedException {
        String newLine = "", date = "", message = "", username = "";
        int index = 0;
        boolean check = false;
        try {
            reader = new BufferedReader(new FileReader("src/storageData.txt"));
            while ((newLine = reader.readLine()) != null) {
                String[] next = newLine.split(",");
                username = next[0];
                message = next[1];
                date = next[2];
                usernames.add(username);
                programUsers.add(new Storage(username, message, date));//takes old users and adds them back into the array

            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //method to allow the user to create a new account
    public static void createAccount() throws InterruptedException {
        String newUser = "";
        do {
            addToUsers();
            try {
                flush();//clears screen before presenting options
                System.out.print("\033[1;36mWhat do you want your username to be [Between 8 - 20 characters;Not case sensitive]: \033[0m");
                newUser = scanner.nextLine();//sets a temporary variable to the userInput that they want as their username
                if (!doesAccountExist(newUser) || newUser.length() < 8 || newUser.length() > 20) {
                    System.out.println("\033[1;91mUsername already exists or/is too long. Please try again\033[0m");
                    continue;
                }
                usernames.add(newUser);//only add usernames here, not yet in the constructor for the object
//                programUsers.add(new Storage(newUser));

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
        do {
            try {
                System.out.print("\033[1;36mWhat is your username: \033[0m");
                username = scanner2.nextLine();
                for (String existingUser: usernames) {//checks if the username matches that of an existing one. If it does, it lets you log in
                    if (existingUser.equalsIgnoreCase(username)) {
                        System.out.println("\033[1;34mUser exists. Proceeding...\033[0m");
                        sleep(1000);
                        userDataMenu(username);//passes username as an argument
                    }
                }
                    System.out.println("\033[1;91mUser not found. Please try again.\033[0m");
                    sleep(1000);

            } catch (InputMismatchException ime) {
                System.out.println("\033[1;91mInput must be a String. Please try again.\033[0m");
                sleep(1000);
            }
        } while (true);

    }

    public static void addToStorage() {
        try {
            writer = new BufferedWriter(new FileWriter("src/storageData.txt"));
            for (Storage current: programUsers) {//loops through the arraylist of values and adds them to the text file
                writer.write(current.getUsername() + "," + current.getMessage() + "," + current.getDate() + "\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void addToUsers() {
        try {
            writer = new BufferedWriter(new FileWriter("src/loginInfo.txt"));
            for (String current: usernames) {//only adds usernames from the logindata text file to the usernames arraylist
                writer.write(current + "\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void userDataMenu(String username) throws InterruptedException {
        int userIndex = 0, messageIndex = 0;
        addToStorage();
        for (int index = 0; index < usernames.size(); index++) {//loop to grab the index of the user currently being accessed. This could also be put into a separate method
            if (username.equalsIgnoreCase(usernames.get(index))) {
                userIndex = index;//save the index that the current username is at
            }
        }
        do {
            System.out.println("\033[1;36mWould you like to:\n1. Access a previous message\n2. Write a new message\n3. Log out");
            switch (scInt.nextInt()) {
                case 1 -> {
                    for (Storage i: programUsers) {//checks to verify if there are messages for that user
                        if (i.getUsername().equalsIgnoreCase(username)) {
                            messageIndex++;//no need to break, instead save the value and carry it into the next method
                        }
                    }
                    if (messageIndex == 0) {
                        System.out.println("\033[1;91mNo past messages to view. Please select another option.\033[0m");
                        sleep(1000);
                    } else {
                        oldMessageList(username, userIndex, messageIndex);
                    }
                }
                case 2 -> newMessage(username, userIndex, messageIndex);
                case 3 -> {
                    System.out.println("\033[1;34mReturning to main menu...\033[0m");
                    sleep(1000);
                    mainMenu();
                }
            }
        } while (true);

    }

    //allows the user to create a new message to be added to the Storage class
    public static void newMessage(String username, int userIndex, int messageIndex) throws InterruptedException {
        String message = "", date = "";
        Pattern pattern;
        Matcher matcher;
        int encryptionType = 0;
        boolean matchFound;
        do {
            try {
                flush();
                //using the pattern and matcher built in methods, the program looks for matches using regex for characters that are not String literals (i.e. numbers or symbols)
                System.out.print("\033[1;36mWhat message would you like to save [Cannot contains symbols or numbers]: \033[0m");
//                message = scanner2.nextLine();//have to use a different scanner because of a program bug immediately causing errors on the first run
                pattern = Pattern.compile(scanner2.nextLine(), Pattern.CASE_INSENSITIVE);
                matcher = pattern.matcher("[^\\s^a-zA-Z]"); //sets the matcher to find any input that is not char a to z and not a whitespace (i.e. a number or symbol)
                matchFound = matcher.find(); //looks for a match
                if (matchFound) {
                    System.out.println(matchFound);
                    System.out.println("\033[1;91mInput cannot contain symbols or numbers. Please try again.\033[0m");
                    sleep(1000);
                } else {
                    System.out.println(matchFound);
                    message = encryption(message, username, messageIndex);
                    date = String.valueOf(new Date());
                    programUsers.add(new Storage(username, message, date));//everytime a new message is created, store as a Storage object with 3 fields. Keep username to remember who wrote the message inside the text file
                    System.out.println("\033[1;32mMessage added. Returning to menu...\033[0m");
                    sleep(1000);
                    userDataMenu(username);
                }
            } catch (InputMismatchException ime) {
                System.out.println("\033[1;91mInput must be a String. Please try again.\033[0m");
                sleep(1000);
            }
        } while (true);

    }

    public static String encryption(String message, String username, int messageIndex) throws InterruptedException {
        int encryptionKey = 0;
        do {
            try {
                System.out.println("\033[1;36mWhat encryption method would you like to use?\n1. Caesar\n2. Atbash\n3. None");
                switch (scInt.nextInt()) {
                    case 1 -> {
                        System.out.println("\033[1;36mHow many keys would you like to shift over for your cipher?\033[0m");
                        encryptionKey = scInt.nextInt();
                        while (encryptionKey < 1 || encryptionKey > 26) {
                            System.out.println("\033[1;91mCipher can only be shifted up atleast 1 key, up to 26. Please try again.\033[0m");
                            encryptionKey = scInt.nextInt();
                        }
                        caesarCiphers.add(new CaesarCipher(username));
                        message = caesarCiphers.get(messageIndex).caesarEncrypt(message, encryptionKey);
                    }
                }
            } catch (InputMismatchException ime) {
                System.out.println("\033[1;91mInput must be an Integer. Please try again.\033[0m");
                sleep(1000);//will automatically loop back; no need to call continue
            }
            return message;
        } while (true);
    }


    public static void oldMessageList(String username, int userIndex, int max) throws InterruptedException{
        int messageIndex = 0, actualIndex = 0;
        String message = "";
        do {
            try {

                flush();//messages must be atleast 1, if less than one, it will just kick you automatically
                System.out.println("\033[1;36mWhat past message would you like to view [1+]: ");
                messageIndex = scInt.nextInt();
                while (messageIndex <= 0 || messageIndex > max) {//Cannot be more messages than there are indexes
                    System.out.println("\033[1;91mInput must be in range [1 to last message]. Please try again\033[0m");
                    messageIndex = scInt.nextInt();
                }
                //decrypt message here
                //print out message from that index, along with the date
                for (Storage sameUser: programUsers) {
                    if (sameUser.getUsername().equalsIgnoreCase(username)) {
                        actualIndex++;//increment this value until it matches the user specified index. (messageIndex)
                        //then, you want to print out the message at that index -> not the actualIndex variable, but the index that the loop is currently at
                        if(messageIndex == actualIndex) {
//                            caesarCiphers.get(actualIndex).caesarDecrypt(message, );
                            System.out.println("User: " + sameUser.getUsername() + " posted: " + sameUser.getMessage() + " at " + sameUser.getDate());
                            System.out.println("\033[1;34mInput any key to continue\033[0m");
                            scanner.next();
                            userDataMenu(username);
                        }
                    }
                }
//                System.out.println(messages.get(messageIndex-1) + " " + dates.get(messageIndex - 1));

            } catch (InputMismatchException ime) {
                System.out.println("\033[1;91mInput must be an Integer. Please try again.\033[0m");
                sleep(1000);//will automatically loop back; no need to call continue
            }
        } while (true);
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