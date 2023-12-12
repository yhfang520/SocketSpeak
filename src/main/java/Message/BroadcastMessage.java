package Message;

import static Message.MessageHardcode.Identifier.BROADCAST_MESSAGE;

import java.io.DataInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * Represents the Broadcast Message
 */
public class BroadcastMessage implements Message{
  private int identifier;
  private String senderUsername;
  private String message;

  /**
   * Constructor for BroadcastMessage
   */
  public BroadcastMessage() {
    this.identifier = BROADCAST_MESSAGE;
  }

  /**
   * Constructor for BroadcastMessage
   *
   * @param senderUsername the sender username
   * @param message  the username
   */
  public BroadcastMessage(String senderUsername, String message) {
    this.identifier = BROADCAST_MESSAGE;
    this.senderUsername = senderUsername;
    this.message = message;
  }

  /**
   * Deserialize the BroadcastMessage from the DataInputStream
   * @param dataInputStream the input stream
   * @return deserialize username
   */
  @Override
  public String deserialize(DataInputStream dataInputStream) {
    try {
      dataInputStream.readChar();

      int senderNameLength  = dataInputStream.readInt();
      dataInputStream.readChar();
      byte[] buffer = new byte[senderNameLength];
      dataInputStream.readFully(buffer);
      senderUsername = new String(buffer, StandardCharsets.UTF_8);
      dataInputStream.readChar();

      int msgLength  = dataInputStream.readInt();
      dataInputStream.readChar();
      buffer = new byte[msgLength];
      dataInputStream.readFully(buffer);
      message = new String(buffer, StandardCharsets.UTF_8);

      return this.toString();

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Serialize the BroadcastMessage to the byte array
   * @return byte array
   */
  @Override
  public byte[] serialize() {
    MessageHandler messageHandler = new MessageHandler();
    byte[] boardcast = messageHandler.join(null,this.identifier);
    boardcast = messageHandler.join(boardcast,senderUsername);
    return messageHandler.join(boardcast,message);
  }

  /**
   * Get the user's name
   * @return the user's name
   */
  public String getUsername() {
    return this.senderUsername;
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
    BroadcastMessage broadcastMessage = (BroadcastMessage) ob;
    return Objects.equals(senderUsername, broadcastMessage.senderUsername)
            && Objects.equals(message, broadcastMessage.message);
  }

  /**
   * Hash code of the BroadcastMessage object
   * @return hash code
   */
  @Override
  public int hashCode() {
    return Objects.hash(identifier, senderUsername, message);
  }

  /**
   * To string show all the BroadcastMessage's info
   * @return string
   */
  @Override
  public String toString() {
    return String.format("(Broadcast Message) %s send to All: %s", getUsername(),message);
  }
}