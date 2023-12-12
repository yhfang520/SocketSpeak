package Message;

import static Message.MessageHardcode.Identifier.DISCONNECT_MESSAGE;
import java.io.DataInputStream;
import java.util.Objects;

/**
 * Represents the Disconnect Message
 */
public class DisconnectMessage extends ConnectMessage{

  /**
   * Constructor for DisconnectMessage
   */
  public DisconnectMessage() {
    super();
    setIdentifier(DISCONNECT_MESSAGE);
  }

  /**
   * Constructor for DisconnectMessage
   *
   * @param username  the username
   */
  public DisconnectMessage(String username) {
    super(username);
    setIdentifier(DISCONNECT_MESSAGE);
  }

  /**
   * Serialize the DisconnectMessage to the byte array
   * @return byte array
   */
  @Override
  public byte[] serialize() {
    return super.serialize();
  }

  /**
   * Deserialize the DisconnectMessage from the DataInputStream
   * @param dataInputStream the input stream
   * @return deserialize username
   */
  @Override
  public String deserialize(DataInputStream dataInputStream) {
    return super.deserialize(dataInputStream);
  }

  /**
   * Get the user's name
   * @return the user's name
   */
  public String getUsername() {
    return super.getUsername();
  }

  /**
   * Get the identifier
   * @return the identifier
   */
  public int getIdentifier() {
    return super.getIdentifier();
  }

  /**
   * Hash code of the DisconnectMessage object
   * @return hash code
   */
  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode());
  }

  /**
   * To string show all the DisconnectMessage's info
   * @return string
   */
  @Override
  public String toString() {
    return String.format("(Disconnect message) %s is disconnecting",getUsername());
  }
}
