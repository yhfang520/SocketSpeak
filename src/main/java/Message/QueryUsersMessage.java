package Message;

import static Message.MessageHardcode.Identifier.QUERY_CONNECTED_USERS;
import java.io.DataInputStream;
import java.util.Objects;

/**
 * Represents the Query Users Message
 */
public class QueryUsersMessage extends ConnectMessage{

  /**
   * Constructor for QueryUsersMessage
   */
  public QueryUsersMessage() {
    setIdentifier(QUERY_CONNECTED_USERS);
  }

  /**
   * Constructor for QueryUsersMessage
   *
   * @param username  the username
   */
  public QueryUsersMessage(String username) {
    super(username);
    setIdentifier(QUERY_CONNECTED_USERS);
  }

  /**
   * Serialize the QueryUsersMessage to the byte array
   * @return byte array
   */
  @Override
  public byte[] serialize() {
    return super.serialize();
  }

  /**
   * Deserialize the QueryUsersMessage from the DataInputStream
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
   * Hash code of the QueryUsersMessage object
   * @return hash code
   */
  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode());
  }

  /**
   * To string show all the QueryUsersMessage's info
   * @return string
   */
  @Override
  public String toString() {
    return String.format("(Query message) %s query all connected users",getUsername());
  }
}
