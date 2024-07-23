public class Storage {

    //this class acts similar to the actual user, storing all user data
    private String username;
    private String date;
    private String message;

    //constructor
    public Storage(String username, String message, String date) {
        this.username = username;
        this.message = message;
        this.date = date;
    }

    //setters were removed as the ability to change these values isn't an option. Only getters are needed
    public String getUsername() {
        return username;
    }

    public String getMessage() {
        return message;
    }

    public String getDate() {
        return date;
    }


}