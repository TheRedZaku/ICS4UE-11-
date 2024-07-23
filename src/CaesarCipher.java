public class CaesarCipher {

    private String username;

    public CaesarCipher(String username) {
        this.username = username;
    }

    public String caesarEncrypt(String messageToEncrypt, int encryptionKey) {
        StringBuilder encryptedMessage = new StringBuilder();
        for (int i = 0; i < messageToEncrypt.length(); i++) {
            if (Character.isUpperCase(messageToEncrypt.charAt(i))){
                char letter = (char)(((int)messageToEncrypt.charAt(i) +
                        encryptionKey - 65) % 26 + 65);
                encryptedMessage.append(letter);
            } else {
                char letter = (char)(((int)messageToEncrypt.charAt(i) +
                        encryptionKey - 97) % 26 + 97);
                encryptedMessage.append(letter);
            }
        }
        return encryptedMessage.toString();
    }

    public String caesarDecrypt(String messageToDecrypt, int encryptionKey){
        StringBuilder decryptedMessage = new StringBuilder();
        for (int i = 0; i < messageToDecrypt.length(); i++) {
            if (Character.isUpperCase(messageToDecrypt.charAt(i))){
                char letter = (char)(((int)messageToDecrypt.charAt(i) - encryptionKey +
                        65) % 26 - 65);
                decryptedMessage.append(letter);
            } else {
                char letter = (char)(((int)messageToDecrypt.charAt(i) - encryptionKey + 97) % 26 - 97);
                decryptedMessage.append(letter);
            }
        }
        return decryptedMessage.toString();
    }
}