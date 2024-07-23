/*
* This program passes through many methods to store and encrypt a message that the user puts into the program. The program includes File I/O as well as many try catches and other error catching methods to ensure all inputs are validated. The messages and usernames are saved to separate text files, for long term storage. Currently, the only encryption method is the Atbash Cipher, because of time constraints.
*
*   @author Kevin Li
*   @since 2024/07/23
*   @version 1.0
*
 */


import java.io.*;//simplified to * as the entire io library is used
import java.util.*;//simplified to * as the entire util library is used

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
    public static Scanner scanner2 = new Scanner(System.in);//second scanner was added due to issues with the program skipping scanner inputs
    public static Scanner scInt = new Scanner(System.in);
    public static ArrayList<String> usernames = new ArrayList<>(); //list of all users.
    public static ArrayList<Storage> programUsers = new ArrayList<>(); // I feel like all the users should be within an object class
//    public static ArrayList<CaesarCipher> caesarCiphers = new ArrayList<>();
    public static ArrayList<AtbashCipher> atbashCiphers = new ArrayList<>();//stores all past and new atbashed ciphers / messages from the user
    //passwords haven't been added, because they would be saved in a text file with no encryption, unless the text file itself was encrypted server side
    public static void main(String[] args) throws InterruptedException {
        mainMenu();//goes out of the program into the main method
    }

    /*
    *
    * This method starts by ensuring all variables handling file data are empty, before adding file data in. It then prompts the user to log in or exit from the program. To reduce nesting, multiple methods were used for user action, instead of clustering inside of one
    *
    *   param1 void
    *
    *   return void
     */
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
                        "\033[1;36m\nWould you like to:\n1. Login\n2. Exit the program");
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

    /*
    *
    * This method is next in the sequence of methods. It asks the user whether they wish to log in with an existing acount that was made, or create a new account. They can also choose to return to the main menu.
    *
    *   param1 void
    *
    *   return void
     */
    //acts as the proxy for the user if they want to create or already have a user
    public static void loggedMenu() throws InterruptedException {
        //This is how we keep data separate and enable the user to traverse / log in and log out while also closing data.
        do {
            try {
                scanner.reset();
                flush();
                System.out.println("\033[1:36mWelcome to the Login Menu" +
                        " Would you like to:\n1. Log into an existing account\n2. Create an account\n3. Return to main menu\033[0m");
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
                scanner.next();
                sleep(1000);
                //end of loop will return to beginning
            }
        } while (true);
    }

    /*
    *
    * This method is simply used to check if the new username that is entered already exists. If it does, it returns true, and otherwise will return false.
    *
    *   param1  String newUser -> the new username that the user wants to create
    *
    *   return boolean -> true or false depending on condition
     */
    //This method checks if the username that the user has entered already exists or not.
    public static boolean doesAccountExist(String newUser) {
        boolean check = false;
            for (String userIndex: usernames) {//linear search algorithm
                if (userIndex.equalsIgnoreCase(newUser)) {//if time permitted, I would have done this as a binary search for Strings
                    check = true;//returns true if the account username already exists
                    break;
                }
            }
        return check;
    }

    /*
    *
    * This method reads from the loginInfo.txt file and adds all the info in there to the usernames.txt file. This allows the program to keep track of what usernames exist.
    *
    *   param1 void
    *
    *   return void
     */
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

    /*
    *
    * This method does a very similar action to that of the readFromFileUsers method, but instead it reads from the storageData.txt, where all the messages are kept. It will then proceed to add them to the arraylist of objects from the Storage class, which acts similar to a user
    *
    *   param1 void
    *
    *   return void
     */
    public static void readFromFileMessageData() {
        String newLine, date, message, username;
        try {
            reader = new BufferedReader(new FileReader("src/storageData.txt"));
            while ((newLine = reader.readLine()) != null) {
                String[] next = newLine.split(",");
                username = next[0];
                message = next[1];
                date = next[2];
                programUsers.add(new Storage(username, message, date));//takes old users and adds them back into the array
                atbashCiphers.add(new AtbashCipher(message));
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    *
    * This method allows the user to create a new account, as long as the username doesn't match a pre-existing one, and is within 8 to 20 characters long
    *
    *   param1 void
    *
    *   return void
     */
    //method to allow the user to create a new account
    public static void createAccount() throws InterruptedException {
        String newUser;
        do {
            try {
                scanner2.reset();
                flush();//clears screen before presenting options
                System.out.print("\033[1;36mWhat do you want your username to be [Between 8 - 20 characters;Not case sensitive]: \033[0m");
                newUser = scanner2.nextLine();//sets a temporary variable to the userInput that they want as their username
                if (doesAccountExist(newUser) || newUser.length() < 8 || newUser.length() > 20) {
                    System.out.println("\033[1;91mUsername already exists or/is too long. Please try again\033[0m");
                    sleep(1000);
                    continue;
                }
                usernames.add(newUser);//only add usernames here, not yet in the constructor for the object
//                programUsers.add(new Storage(newUser));
                System.out.println("\033[1;32mAccount created. Returning to main menu...\033[0m");
                sleep(1000);
                addToUsers();
                mainMenu();
            } catch (InputMismatchException ime) {
                System.out.println("\033[1;91mInput must be a String. Please try again.\033[0m");
                sleep(1000);
            }
        } while (true);
    }

    /*
    *
    * This method allows the user to log into an exisiting account. If no accounts exist yet, it will prematurely kick you out. Otherwise, if the account you enter doesn't match an account in the system, it will prompt you to re-enter the username.
    *
    *   param1 void
    *
    *   return void
     */
    //method to allow a user to log into an account they just/already created
    public static void logIn() throws InterruptedException {
        String username;
        do {
            try {
                flush();
                if (usernames.isEmpty()) {
                    System.out.println("\033[1;91mNo accounts exist. Please try again.\033[0m");
                    sleep(1000);
                    loggedMenu();
                }
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

    /*
    *
    * This method will add any newly inputted values to the text file for messages 'storageData.txt.' This is only called whenever a new message is added, so that the program saves it for the next time the program is run.
    *
    *   param1 void
    *
    *   return void
     */
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

    /*
    *
    * This method performs very similarly to the addToStorage method, except it adds data to the 'loginInfo.txt' file instead.
    *
    *   param1 void
    *
    *   return void
     */
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

    /*
    *
    * This method is entered after the user finally logs in using their username. It will keep track of the username the entire time the user is logged into this account. As passwords were omitted for brevity, it was not included. This method prompts the user on whether they want to add a new message, access an older one, or simply log back out.
    *
    *   param1 String username -> the username of the account currently logged into
    *
    *   return void
     */
    public static void userDataMenu(String username) throws InterruptedException {
        int messageIndex = 0;
        addToStorage();
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
                        oldMessageList(username, messageIndex);
                    }
                }
                case 2 -> newMessage(username, messageIndex);
                case 3 -> {
                    System.out.println("\033[1;34mReturning to main menu...\033[0m");
                    sleep(1000);
                    mainMenu();
                }
                default -> {
                    System.out.println("\033[1;91mOption not found. Please try again.\033[0m");
                    sleep(1000);
                }
            }
        } while (true);

    }

    /*
    *
    * This method allows the user to create a new message that can be added to the text file. The message uses a Regex(Regular Expression) to validate whether it contains numbers of symbols, as they cannot be encrypted with the Atbash cipher. If it does, the user is reprompted. Afterwords, the message segment is encrypted and the username, message, and date are added to an arraylist of storage objects. The user is then returned to the userDataMenu.
    *
    *   param1 String username -> the current user who is writing a message
    *   param2 int messageIndex -> the index that the message is kept, as more than one user is saving their message inside of the same text file.
    *
    *   return void
     */
    //allows the user to create a new message to be added to the Storage class
    public static void newMessage(String username, int messageIndex) throws InterruptedException {
        boolean complete = false;
        String message, date;
        do {
            try {
                flush();
                //using the pattern and matcher built in methods, the program looks for matches using regex for characters that are not String literals (i.e. numbers or symbols)
                System.out.print("\033[1;36mWhat message would you like to save [Cannot contains symbols or numbers]: \033[0m");
                message = scanner2.nextLine();//have to use a different scanner because of a program bug immediately causing errors on the first run
//                pattern = Pattern.compile(Pattern.quote(message), Pattern.CASE_INSENSITIVE);
//                matcher = pattern.matcher("[^\\sa-z]"); //sets the matcher to find any input that is not char a to z and not a whitespace (i.e. a number or symbol)
//                matchFound = matcher.find(); //looks for a match
                if (message.matches(".*[^\\s\\p{L}].*")) {
//                    System.out.println(matchFound);
                    System.out.println("\033[1;91mInput cannot contain symbols or numbers. Please try again.\033[0m");
                    sleep(1000);
                } else {
//                    System.out.println(matchFound);
                    message = encryption(message, username, messageIndex);
                    date = String.valueOf(new Date());
                    programUsers.add(new Storage(username, message, date));//everytime a new message is created, store as a Storage object with 3 fields. Keep username to remember who wrote the message inside the text file
                    System.out.println("\033[1;32mMessage added. Returning to menu...\033[0m");
                    sleep(1000);
                    complete = true;
                    userDataMenu(username);
                }
            } catch (InputMismatchException ime) {
                System.out.println("\033[1;91mInput must be a String. Please try again.\033[0m");
                sleep(1000);
            }
        } while (!complete);

    }

    /*
    *
    * This method takes the message that is being encrypted, saves the index in the file, and the username of the current user to encrypt the message and return it as a string.
    *
    *   param1 String message -> message to be encrypted
    *   param2 String username -> current user
    *   param3 int messageIndex -> index that the message will be kept in the arrayList / text file
    *
    *   return String -> the encrypted message
     */
    public static String encryption(String message, String username, int messageIndex) throws InterruptedException {
        do {
            try {//originally wanted to do 3 ciphers but due to the time crunch and difficulties, I will only implement one. This should still get the idea across
                atbashCiphers.add(new AtbashCipher(username));
                message = atbashCiphers.get(messageIndex).atbashEncrypt(message);

            } catch (InputMismatchException ime) {
                System.out.println("\033[1;91mInput must be an Integer. Please try again.\033[0m");
                sleep(1000);//will automatically loop back; no need to call continue
            }
            return message;
        } while (true);
    }


    /*
    *
    * This message allows the user to selectively view a message that they previously added, as long as the user has any messages to view in the first place. The index must go from the first message inputted, to the last message of the index for that user.
    *
    *   param1 String username -> current user
    *   param2 int max -> the max array length of the index
    *
    *   return void
     */
    public static void oldMessageList(String username, int max) throws InterruptedException{
        int messageIndex, actualIndex = 0;
        String message;
        do {
            try {

                flush();//messages must be atleast 1, if less than one, it will just kick you automatically
                System.out.println("\033[1;36mWhat past message would you like to view [1+]: \033[0m");
                messageIndex = scInt.nextInt();
                while (messageIndex <= 0 || messageIndex > max) {//Cannot be more messages than there are indexes
                    System.out.println("\033[1;91mInput must be in range [1 to " + max + "]. Please try again\033[0m");
                    messageIndex = scInt.nextInt();
                }
                for (Storage sameUser: programUsers) {
                    if (sameUser.getUsername().equalsIgnoreCase(username)) {
                        actualIndex++;//increment this value until it matches the user specified index. (messageIndex)
                        //then, you want to print out the message at that index -> not the actualIndex variable, but the index that the loop is currently at
                        if(messageIndex == actualIndex) {
                            message = atbashCiphers.get(actualIndex - 1).atbashDecrypt(sameUser.getMessage());
                            System.out.println("User: " + sameUser.getUsername() + " posted: " + message + " at " + sameUser.getDate());
                            System.out.println("\033[1;34mInput any key to continue\033[0m");
                            scanner2.next();
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

    /*
    *
    * This method uses the ASCII codes of the terminal to clear the screen of any text previously. This increases readability and decreases the clutter of error mesasges or the program when running.
    *
    *   param1 void
    *
    *   return void
     */
    //method will clear the console whenever called
    public static void flush() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    /*
    *
    * This method uses the built-in Threads of the CPU to cause a stop in the execution of the program for a finite number of milliseconds. This is usually used to display error messages for a short period of time before clearing them.
    *
    *   param1 int milliseconds -> the specified unit of time that you want the program to stop for
    *
    *   return void
     */
    //method causes the program to pause for finite determined time period; usually to show errors and then clear them to reduce clutter
    public static void sleep(int milliseconds) throws InterruptedException {
        Thread.sleep(milliseconds);
        //this causes an interrupted exception in the program as you interrupt an execution -> that is why throwing an InterruptedException is necessary so that the program knows that it's intentional
    }


}
