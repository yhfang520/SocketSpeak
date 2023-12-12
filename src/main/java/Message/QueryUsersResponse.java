package Message;

import static Message.MessageHardcode.Identifier.QUERY_USER_RESPONSE;

import java.io.DataInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Represents the Query Users Response
 */
public class QueryUsersResponse implements Message {

  private int identifier;
  private ArrayList<String> usernames;

  /**
   * Constructor for QueryUsersResponse
   */
  public QueryUsersResponse() {
    this.identifier = QUERY_USER_RESPONSE;
    this.usernames = new ArrayList<>();
  }

  /**
   * Constructor for QueryUsersResponse
   *
   * @param usernames  the user's name
   */
  public QueryUsersResponse(ArrayList<String> usernames) {
    this.identifier = QUERY_USER_RESPONSE;
    this.usernames = usernames;
  }

  /**
   * Deserialize the QueryUsersResponse from the DataInputStream
   * @param dataInputStream the input stream
   * @return deserialize username
   */
  @Override
  public String deserialize(DataInputStream dataInputStream) {
    try {
      dataInputStream.readChar();
      int numberOfUsers = dataInputStream.readInt();

      for (int i = 0; i < numberOfUsers; i++) {
        dataInputStream.readChar();
        int userNameLength = dataInputStream.readInt();
        dataInputStream.readChar();
        byte[] buffer = new byte[userNameLength];
        dataInputStream.readFully(buffer);
        String username = new String(buffer, StandardCharsets.UTF_8);
        this.usernames.add(username);
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return this.toString();
  }

  /**
   * Serialize the QueryUsersResponse to the byte array
   * @return byte array
   */
  @Override
  public byte[] serialize() {
    MessageHandler messageHandler = new MessageHandler();
    byte[] queryResponse = messageHandler.join(null, this.identifier);
    queryResponse = messageHandler.join(queryResponse, getNumberOfUsers());

    queryResponse = messageHandler.join(queryResponse,usernames.toArray(new String[0]));
    return queryResponse;
  }

  /**
   * Get the identifier
   * @return the identifier
   */
  @Override
  public int getIdentifier() {
    return identifier;
  }

  /**
   * Get the number of users
   * @return the number of users
   */
  public int getNumberOfUsers() {
    return this.usernames.size();
  }

  /**
   * Make sure the QueryUsersResponse objects are equals
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
    QueryUsersResponse queryUsersResponse = (QueryUsersResponse) ob;
    return Objects.equals(usernames, queryUsersResponse.usernames);
  }

  /**
   * Hash code of the QueryUsersResponse object
   * @return hash code
   */
  @Override
  public int hashCode() {
    return Objects.hash(usernames);
  }

  /**
   * To string show all the QueryUsersResponse's info
   * @return all connected users
   */
  @Override
  public String toString() {
    return String.format("(Query Response) %d users found:\n"+
            String.join("\n",usernames.toArray(new String[0])), getNumberOfUsers());
  }
}