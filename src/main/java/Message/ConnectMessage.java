package Message;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static Message.MessageHardcode.Identifier.CONNECT_MESSAGE;

/**
 * Represents the Connect Message
 */
public class ConnectMessage implements Message{

    private int identifier;
    private String username;

    /**
     * Constructor for ConnectMessage
     */
    public ConnectMessage() {
        this.identifier = CONNECT_MESSAGE;
    }

    /**
     * Constructor for ConnectMessage
     *
     * @param username  the user's name
     */
    public ConnectMessage(String username) {
        this.identifier = CONNECT_MESSAGE;
        this.username = username;
    }

    /**
     * Serialize the ConnectMessage to the byte array
     * @return byte array
     */
    @Override
    public byte[] serialize(){
        MessageHandler messageHandler = new MessageHandler();
        byte[] connectMsg = messageHandler.join(null,this.identifier);
        return messageHandler.join(connectMsg,username);
    }

    /**
     * Deserialize the ConnectMessage from the DataInputStream
     * @param dataInputStream the input stream
     * @return deserialize username
     */
    @Override
    public String deserialize(DataInputStream dataInputStream){
        try {
//          dataInputStream.readInt();
            dataInputStream.readChar();
            int userNameLength  = dataInputStream.readInt();
            dataInputStream.readChar();
            byte[] buffer = new byte[userNameLength];
            dataInputStream.readFully(buffer);
            String username = new String(buffer, StandardCharsets.UTF_8);
            this.username =  username;
            return this.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get the user's name
     * @return the user's name
     */
    public String getUsername() {
        return username;
    }

    /**
     * Set the identifier
     * @param identifier  the identifier
     */
    public void setIdentifier(int identifier) {
        this.identifier = identifier;
    }

    /**
     * Get the identifier
     * @return the identifier
     */
    public int getIdentifier() {
        return this.identifier;
    }

    /**
     * Make sure the BroadcastMessage objects are equals
     * @return true if it's all equal
     */
    @Override
    public boolean equals(Object ob) {
        if(this == ob) {
            return true;
        }
        if(ob == null || getClass() != ob.getClass()) {
            return false;
        }
        ConnectMessage connectMessage = (ConnectMessage) ob;
        return Objects.equals(username, connectMessage.username);
    }

    /**
     * Hash code of the BroadcastMessage object
     * @return hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

    /**
     * To string show all the ConnectMessage's info
     * @return string
     */
    @Override
    public String toString() {
        return String.format("(Connect message) %s is connecting to",getUsername());
    }
}
