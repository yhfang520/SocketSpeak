package Message;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static Message.MessageHardcode.Identifier.*;
import static Message.MessageHardcode.DELIMITER;

/**
 * Represents the Message Handler class to create a different types of message
 */
public class MessageHandler {

  /**
   * Constructor for MessageHandler
   */
  public MessageHandler() {
  }

  /**
   * Create the message based on the identifier
   * @param identifier the identifier
   * @return the message or null if the identifier is invalid
   */
  public Message create(int identifier) {
    switch (identifier) {
      case CONNECT_MESSAGE:
        return (Message) new ConnectMessage();
      case DISCONNECT_MESSAGE:
        return (Message) new DisconnectMessage();
      case QUERY_CONNECTED_USERS:
        return (Message) new QueryUsersMessage();
      case DIRECT_MESSAGE:
        return (Message) new DirectMessage();
      case BROADCAST_MESSAGE:
        return (Message) new BroadcastMessage();
      case SEND_INSULT:
        return (Message) new InsultMessage();
      case CONNECT_RESPONSE:
        return (Message) new ConnectResponse();
      case QUERY_USER_RESPONSE:
        return (Message) new QueryUsersResponse();
      case FAILED_MESSAGE:
        return (Message) new FailMessage();
    }
    return null;
  }

  /**
   * Create Connect, Disconnect, Query Message
   * @param identifier  the identifier
   * @param username  the user's name
   * @return the message or null if the identifier is invalid
   */
  public Message create(int identifier, String username) {
    switch (identifier) {
      case CONNECT_MESSAGE:
        return (Message) new ConnectMessage(username);
      case DISCONNECT_MESSAGE:
        return (Message) new DisconnectMessage(username);
      case QUERY_CONNECTED_USERS:
        return (Message) new QueryUsersMessage(username);
    }
    return null;
  }

  /**
   * Create Direct Message or insult message
   * @param identifier  the identifier
   * @param senderUsername  the sender username
   * @param recipientUsername the recipient username
   * @param message the message
   * @return the message or null if the identifier is invalid
   */
  public Message create(int identifier, String senderUsername, String recipientUsername, String message) {
    switch (identifier){
      case DIRECT_MESSAGE:
        return (Message) new DirectMessage(senderUsername, recipientUsername, message);
      case SEND_INSULT:
        return (Message) new InsultMessage(senderUsername, recipientUsername, "");
    }
    return null;
  }

  /**
   * Create Broadcast Message
   * @param senderUsername  the sender username
   * @param message the message
   * @return the broadcast message
   */
  public Message create(String senderUsername, String message) {
    return (Message) new BroadcastMessage(senderUsername, message);
  }

  /**
   * Create Query Response
   * @param usernames the username
   * @return the query message
   */
  public Message createResponse(ArrayList<String> usernames) {
    return (Message) new QueryUsersResponse(usernames);
  }

  /**
   * Create fail message
   * @param message the message
   * @return the fail message
   */
  public Message createFail(String message) {
    return (Message) new FailMessage(message);
  }

  /**
   * Create connect, disconnect response using same class
   * @param success true if it's success
   * @param message the message
   * @return the response message
   */
  public Message createResponse(boolean success, String message) {
        return (Message) new ConnectResponse(success, message);
  }

  /**
   * Join a variable number of string to origin byte array
   * @param origin  the original byte array
   * @param strs varargs (variable-length argument) String
   * @return  the joined byte array
   */
  public byte[] join(byte[] origin, String... strs) {
    try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream)) {

      dataOutputStream.write(origin);
      for (int i = 0; i < strs.length; i++) {
        dataOutputStream.writeChar(DELIMITER);
        byte[] byteOfStr = strs[i].getBytes(StandardCharsets.UTF_8);
        dataOutputStream.writeInt(byteOfStr.length);
        dataOutputStream.writeChar(DELIMITER);
        dataOutputStream.write(byteOfStr);
      }
      byte[] joinMsg = byteArrayOutputStream.toByteArray();
      return joinMsg;

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Join an integer to the original byte array
   * @param origin  the original byte array
   * @param num  the number
   * @return the joined byte array
   */
  public byte[] join(byte[] origin, int num) {
    try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream)) {

      if (origin != null) {
        dataOutputStream.write(origin);
        dataOutputStream.writeChar(DELIMITER);
      }
      dataOutputStream.writeInt(num);

      return byteArrayOutputStream.toByteArray();

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Join a boolean to the original byte array
   * @param origin  the original byte array
   * @param bool  the boolean to join
   * @return the joined byte array
   */
  public byte[] join(byte[] origin, boolean bool) {
    try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream)) {

      dataOutputStream.write(origin);
      dataOutputStream.writeChar(DELIMITER);
      dataOutputStream.writeBoolean(bool);

      return byteArrayOutputStream.toByteArray();

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}