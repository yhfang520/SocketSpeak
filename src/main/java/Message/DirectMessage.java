package Message;

import static Message.MessageHardcode.Identifier.DIRECT_MESSAGE;
import static Message.MessageHardcode.DELIMITER;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * Represents the Direct Message
 */
public class DirectMessage implements Message{

  private int identifier;
  private String senderUsername;
  private String recipientUsername;
  private String message;

  /**
   * Constructor for DirectMessage
   */
  public DirectMessage() {
    this.identifier = DIRECT_MESSAGE;
  }

  /**
   * Constructor for DirectMessage
   *
   * @param senderUsername      the sender username
   * @param recipientUsername   the recipient username
   * @param message             the message
   */
  public DirectMessage(String senderUsername, String recipientUsername, String message) {
    this.identifier = DIRECT_MESSAGE;
    this.senderUsername = senderUsername;
    this.recipientUsername = recipientUsername;
    this.message = message;
  }

  /**
   * Serialize the DirectMessage to the byte array
   * @return byte array
   */
  @Override
  public byte[] serialize(){
    MessageHandler messageHandler = new MessageHandler();
    byte[] header = messageHandler.join(null,getIdentifier());
    return messageHandler.join(header,getUsername(),getRecipientUsername(),getMessage());
  }

  /**
   * Deserialize the DirectMessage from the DataInputStream
   * @param dataInputStream the input stream
   * @return deserialize username
   */
  @Override
  public String deserialize(DataInputStream dataInputStream){
    try {
      dataInputStream.readChar();

      int senderNameLength  = dataInputStream.readInt();
      dataInputStream.readChar();
      byte[] buffer = new byte[senderNameLength];
      dataInputStream.readFully(buffer);
      setSenderUsername(new String(buffer, StandardCharsets.UTF_8));
      dataInputStream.readChar();

      int recipientNameLength  = dataInputStream.readInt();
      dataInputStream.readChar();
      buffer = new byte[recipientNameLength];
      dataInputStream.readFully(buffer);
      setRecipientUsername(new String(buffer, StandardCharsets.UTF_8));
      dataInputStream.readChar();

      int msgLength  = dataInputStream.readInt();
      dataInputStream.readChar();
      buffer = new byte[msgLength];
      dataInputStream.readFully(buffer);
      setMessage(new String(buffer, StandardCharsets.UTF_8));

      return this.toString();

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Serialize sequential
   * @return byte direct message
   */
  public byte[] serializeSequential(){
    try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream)) {

      dataOutputStream.writeInt(identifier);
      dataOutputStream.writeChar(DELIMITER);

      byte[] byteSenderName = senderUsername.getBytes(StandardCharsets.UTF_8);
      dataOutputStream.writeInt(byteSenderName.length);
      dataOutputStream.writeChar(DELIMITER);
      dataOutputStream.write(byteSenderName);
      dataOutputStream.writeChar(DELIMITER);

      byte[] byteRecipientName = recipientUsername.getBytes(StandardCharsets.UTF_8);
      dataOutputStream.writeInt(byteRecipientName.length);
      dataOutputStream.writeChar(DELIMITER);
      dataOutputStream.write(byteRecipientName);
      dataOutputStream.writeChar(DELIMITER);


      byte[] byteMsg = message.getBytes(StandardCharsets.UTF_8);
      dataOutputStream.writeInt(byteMsg.length);
      dataOutputStream.writeChar(DELIMITER);
      dataOutputStream.write(byteMsg);


      byte[] byteDirectMsg = byteArrayOutputStream.toByteArray();
      return byteDirectMsg;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Get the identifier
   * @return the identifier
   */
  public int getIdentifier() {
    return this.identifier;
  }

  /**
   * Get the user's name
   * @return the user's name
   */
  public String getUsername() {
    return this.senderUsername;
  }

  /**
   * Get the recipient username
   * @return the recipient username
   */
  public String getRecipientUsername() {
    return recipientUsername;
  }

  /**
   * Get the message
   * @return the message
   */
  public String getMessage() {
    return message;
  }

  /**
   * Set the identifier
   * @param identifier  the identifier
   */
  public void setIdentifier(int identifier) {
    this.identifier = identifier;
  }

  /**
   * Set the sender username
   * @param senderUsername  the sender username
   */
  public void setSenderUsername(String senderUsername) {
    this.senderUsername = senderUsername;
  }

  /**
   * Set the recipient username
   * @param recipientUsername  the recipient username
   */
  public void setRecipientUsername(String recipientUsername) {
    this.recipientUsername = recipientUsername;
  }

  /**
   * Set the message
   * @param message  the message
   */
  public void setMessage(String message) {
    this.message = message;
  }

  /**
   * Make sure the DirectMessage objects are equals
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
    DirectMessage directMessage = (DirectMessage) ob;
    return Objects.equals(senderUsername, directMessage.senderUsername)
            && Objects.equals(recipientUsername, directMessage.recipientUsername)
            && Objects.equals(message, directMessage.message);
  }

  /**
   * Hash code of the DirectMessage object
   * @return hash code
   */
  @Override
  public int hashCode() {
    return Objects.hash(senderUsername, recipientUsername, message);
  }

  /**
   * To string show all the DirectMessage's info
   * @return string
   */
  @Override
  public String toString() {
    return String.format("(Direct Message) %s send to %s: %s", getUsername(),getRecipientUsername(),getMessage());
  }
}
