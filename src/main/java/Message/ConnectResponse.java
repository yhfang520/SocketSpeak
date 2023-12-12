package Message;

import static Message.MessageHardcode.Identifier.CONNECT_RESPONSE;

import java.io.DataInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * Represents the Connect Response
 */
public class ConnectResponse implements Message {

  private int identifier;
  private boolean success;
  private String message;

  /**
   * Constructor for ConnectResponse
   */
  public ConnectResponse() {
    this.identifier = CONNECT_RESPONSE;
  }

  /**
   * Constructor for ConnectResponse
   *
   * @param success   the success
   * @param message   the message
   */
  public ConnectResponse(boolean success, String message) {
    this.identifier = CONNECT_RESPONSE;
    this.success = success;
    this.message = message;
  }

  /**
   * Deserialize the ConnectResponse from the DataInputStream
   * @param dataInputStream the input stream
   * @return deserialize username
   */
  @Override
  public String deserialize(DataInputStream dataInputStream) {
    try {
      dataInputStream.readChar();
      this.success  = dataInputStream.readBoolean();
      dataInputStream.readChar();

      int msgLength  = dataInputStream.readInt();
      dataInputStream.readChar();
      byte[] buffer = new byte[msgLength];
      dataInputStream.readFully(buffer);
      this.message = new String(buffer, StandardCharsets.UTF_8);

      return this.toString();

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Serialize the ConnectResponse to the byte array
   * @return byte array
   */
  @Override
  public byte[] serialize() {
    MessageHandler messageHandler = new MessageHandler();
    byte[] msg = messageHandler.join(null,this.identifier);
    msg =  messageHandler.join(msg,this.success);
    return messageHandler.join(msg,this.message);
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
   * Display is success
   * @return the success
   */
  public boolean isSuccess() {
    return success;
  }

  /**
   * Make sure the ConnectResponse objects are equals
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
    ConnectResponse connectResponse = (ConnectResponse) ob;
    return Objects.equals(success, connectResponse.success)
            && Objects.equals(message, connectResponse.message);
  }

  /**
   * Hash code of the ConnectResponse object
   * @return hash code
   */
  @Override
  public int hashCode() {
    return Objects.hash(success, message);
  }

  /**
   * To string show all the ConnectResponse's info
   * @return string
   */
  @Override
  public String toString() {
    return String.format(
            "connection response: success is %s, %s",isSuccess(),getMessage());
  }
}