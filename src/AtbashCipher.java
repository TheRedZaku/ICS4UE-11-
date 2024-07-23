public class AtbashCipher{
    private String messagetoEncrypt;
    private String messagetoDecrypt = atbashEncrypt();


    public AtbashCipher(String messagetoEncrypt){
        this.messagetoEncrypt = messagetoEncrypt;
    }

    public String atbashEncrypt() {
        StringBuilder encryptedMessage = new StringBuilder();
        for(char letter : messagetoEncrypt.toCharArray()){
            if (letter != ' ') {
                if(Character.isLetter(letter)){
                    if (Character.isUpperCase(letter)) {
                        encryptedMessage.append((char) ('Z' - (letter - 'A')));
                    } else {
                        encryptedMessage.append((char) ('z' - (letter - 'a')));
                    }
                }
                else{
                    encryptedMessage.append(letter);
                }
            }
            else{
                encryptedMessage.append(" ");
            }
        }
        return encryptedMessage.toString();
    }

    public String atbashDecrypt(){
        StringBuilder decryptedMessage = new StringBuilder();
        for(char letter : messagetoDecrypt.toCharArray()){
            if (letter != ' ') {
                if(Character.isLetter(letter)){
                    if (Character.isUpperCase(letter)) {
                        decryptedMessage.append((char) ('A' + (letter + 'Z')));
                    } else {
                        decryptedMessage.append((char) ('a' + (letter + 'z')));
                    }
                }
                else{
                    decryptedMessage.append(letter);
                }
            }
            else{
                decryptedMessage.append(" ");
            }
        }
        return decryptedMessage.toString();
    }
}