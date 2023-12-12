package Message;

import static Message.MessageHardcode.Identifier.FAILED_MESSAGE;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * Represents the Failed Message
 */
public class FailMessage implements Message{
  private int identifier;
  private String message;

  /**
   * Constructor for FailedMessage
   */
  public FailMessage() {
    this.identifier = FAILED_MESSAGE;
  }

  /**
   * Constructor for FailedMessage
   *
   * @param message   the message
   */
  public FailMessage(String message) {
    this.identifier = FAILED_MESSAGE;
    this.message = message;
  }

  /**
   * Deserialize the FailMessage from the DataInputStream
   * @param dataInputStream the input stream
   * @return deserialize username
   */
  @Override
  public String deserialize(DataInputStream dataInputStream) {
    try {
      dataInputStream.readChar();
      int msgLength = dataInputStream.readInt();
      dataInputStream.readChar();
      byte[] buffer = new byte[msgLength];
      dataInputStream.readFully(buffer);
      message = new String(buffer, StandardCharsets.UTF_8);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }


    return this.toString();
  }

  /**
   * Serialize the FailMessage to the byte array
   * @return byte array
   */
  @Override
  public byte[] serialize() {
    MessageHandler messageHandler = new MessageHandler();
    byte[] connectMsg = messageHandler.join(null,this.identifier);
    return messageHandler.join(connectMsg,message);
  }

  /**
   * Get the message
   * @return the message
   */
  public String getMessage() {
    return message;
  }

  /**
   * Get the identifier
   * @return the identifier
   */
  public int getIdentifier() {
    return this.identifier;
  }

  /**
   * Make sure the FailMessage objects are equals
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
    FailMessage failMessage = (FailMessage) ob;
    return Objects.equals(message, failMessage.message);
  }

  /**
   * Hash code of the FailMessage object
   * @return hash code
   */
  @Override
  public int hashCode() {
    return Objects.hash(message);
  }

  /**
   * To string show all the FailMessage's info
   * @return string
   */
  @Override
  public String toString() {
    return String.format("(Fail message) %s",message);
  }
}