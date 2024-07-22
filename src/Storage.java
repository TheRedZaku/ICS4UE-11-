public class Storage {

    //attributes
    private String userInfo;
    private String username;
    private int encryptiontype;


    public Storage(String username, int encryptiontype) {
        this.username = username;
        this.encryptiontype = encryptiontype;
    }

    public String getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(String userInfo) {
        this.userInfo = userInfo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
