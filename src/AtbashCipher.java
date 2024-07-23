
/**
 * Class creates an atbash cipher that can encrypt and decrypt 
 messages
 * Bugs: None Known
 *
 * @author Aarush Behal
 * @since 2024-07-23
 * @version 1.0
 */
public class AtbashCipher{
    // Instance variables
    private String messageToEncrypt;

    /**
     * Constructor method of the class to initialize instance variables
     */
    public AtbashCipher(String messageToEncrypt){
        this.messageToEncrypt = messageToEncrypt;
    }

    /**
     * Public method that encrypts the message
     * @return String encrypted message
     */
    public String atbashEncrypt(String messageToEncrypt) {
        StringBuilder encryptedMessage = new StringBuilder();

        // Loop through each character in the message
        for(char letter : messageToEncrypt.toCharArray()){

            // Checks if the character is not a space 
            if (letter != ' ') {

                // CHecks if the character is a letter
                if(Character.isLetter(letter)){

                    // Checks if the character is uppercase letter and encrypts character before adding to StringBuilder
                    if (Character.isUpperCase(letter)) {
                        encryptedMessage.append((char) ('Z' - (letter - 'A')));
                    }
                    // Means the character is lowercase letter and encrypts character before adding to StringBuilder
                    else {
                        encryptedMessage.append((char) ('z' - (letter - 'a')));
                    }

                }
                // Otherwise it just adds the letter to the StringBuilder
                else{
                    encryptedMessage.append(letter);
                }

            }
            // Means character is a whitespace and adds to the StringBuilder
            else{
                encryptedMessage.append(" ");
            }

        }

        return encryptedMessage.toString();
    }

    /**
     * Public method that decrypts the message using the encryption     
     method
     * @return String decrypted message
     */
    public String atbashDecrypt(String messageToDecrypt) {
        return atbashEncrypt(messageToDecrypt);
    }
}