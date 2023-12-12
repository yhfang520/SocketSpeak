package Server;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import Message.*;
import java.util.Iterator;

import randomInsult.RandomInsultGenerator;
import static Message.MessageHardcode.Identifier.*;


/**
 * This class will handle just one Client Connection handle communication with this user.
 */
public class ClientConnectionHandler implements Runnable {

  private String clientName;
  private Socket clientSocket;
  private UsersSocketManager usersSocketManager;
  private InputStream inputStream;
  private OutputStream outputStream;
  private MessageHandler messageHandler;

  /**
   * The init identifier
   */
  public static final int INIT_IDENTIFIER = -1;

  /**
   * The server name
   */
  public static final String SERVER_NAME = "[Server]";

  /**
   * Data input stream
   */
  public DataInputStream dataInputStream;

  /**
   * Constructor for ClientConnectionHandler
   *
   * @param clientSocket              the socket for communication with client
   * @param usersSocketManager        the users socket manager
   */
  public ClientConnectionHandler(Socket clientSocket, UsersSocketManager usersSocketManager) {
    this.clientSocket = clientSocket;
    try {
      this.inputStream = this.clientSocket.getInputStream();
      this.dataInputStream = new DataInputStream(this.inputStream);
      this.outputStream = this.clientSocket.getOutputStream();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    this.messageHandler = new MessageHandler();
    this.usersSocketManager = usersSocketManager;

  }

  /**
   * If connect success, add client to connected users database server continue handle new messages until server stop
   */
  @Override
  public void run() {
    if (connect()) {
      handleMessages();
    }
    try {
      this.clientSocket.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    System.out.println(clientName+" exited from the chat room");
  }

  /**
   * Continue handle new messages if receive a disconnect message, server will end this client connection thread.
   */
  public void handleMessages() {
    try {
      int identifier = INIT_IDENTIFIER;
      while (true & identifier != DISCONNECT_MESSAGE) {
        if (dataInputStream.available() <= 0) {
          continue;
        }
        System.out.println(SERVER_NAME + " received a message:");

        identifier = dataInputStream.readInt();
        execute(identifier, dataInputStream);
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Wait until receive connect message send connect response
   * @return if connect success
   */
  public boolean connect() {
    boolean success = false;
    try {
      String responseMsg = "should be a connect response";
      while (true) {
        if (dataInputStream.available() > 0) {
          break;
        }
      }

      int identifier = dataInputStream.readInt();
      if (identifier != CONNECT_MESSAGE) {
        responseMsg = "Connect failed, did not send a valid connect message";
        Message fail = messageHandler.createFail(responseMsg);
        send(fail);
        return false;
      }

      ConnectMessage connectMessage = new ConnectMessage();
      System.out.println(connectMessage.deserialize(dataInputStream));
      this.clientName = connectMessage.getUsername();

      if (this.usersSocketManager.contains(clientName)) {
        responseMsg = String.format(
            "you were already connected, there are %d other connected clients",
            usersSocketManager.queryNum());
        success = false;
      }
      else if (this.usersSocketManager.add(clientName, clientSocket)) {
        responseMsg = String.format(
            "Connect was successful, there are %d other connected clients",
            usersSocketManager.queryNum());
        success = true;
      } else{
        responseMsg = String.format("Connect failed, there are up to %d other connected clients",
            usersSocketManager.queryNum());
        success = false;
      }

      if(success){
      Message response = messageHandler.createResponse(success, responseMsg);
      send(response);}
      else {
        Message fail  = messageHandler.createFail(responseMsg);
        send(fail);
      }

    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }
    return success;
  }

  /**
   * Disconnect with client remove client socket from connected users hashmap,
   * stop socket communicate and quit client connection thread
   * @return if connect success
   */
  private boolean disconnect() {
    boolean success = true;
    String responseStr = "You are no longer connected";
    if (!usersSocketManager.remove(clientName)) {
      success = false;
      responseStr = "Disconnect failed, You were not connected";
    }
    Message disconnectResponse = messageHandler.createResponse(success, responseStr);
    send(disconnectResponse);
    return success;
  }

  /**
   * Handle different messages request from client given message type and input stream
   * execute different requests like query, direct, or broadcast generate response messages and send back to client
   * @param identifier the identifier of the message
   * @param dataInputStream the input stream from which to read to message data
   */
  private void execute(int identifier, DataInputStream dataInputStream) {
    String responseMsgStr = null;

    if (identifier == DISCONNECT_MESSAGE) {
      DisconnectMessage disconnectMessage = new DisconnectMessage();
      System.out.println(disconnectMessage.deserialize(dataInputStream));
      disconnect();
    }
    else if (identifier == QUERY_CONNECTED_USERS) {
      QueryUsersMessage queryUsersMessage = new QueryUsersMessage();
      System.out.println(queryUsersMessage.deserialize(dataInputStream));
      Message responseMsg = messageHandler.createResponse(this.usersSocketManager.queryAll());
      send(responseMsg);
    }
    else if (identifier == DIRECT_MESSAGE) {
      DirectMessage directMessage = new DirectMessage();
      System.out.println(directMessage.deserialize(dataInputStream));
      responseMsgStr = send(directMessage.getRecipientUsername(), directMessage);
    }
    else if (identifier == SEND_INSULT) {
      InsultMessage insultMessage = new InsultMessage();
      System.out.println(insultMessage.deserialize(dataInputStream));
      responseMsgStr = sendInsult(insultMessage);
    }
    else if (identifier == BROADCAST_MESSAGE) {
      BroadcastMessage broadcastMessage = new BroadcastMessage();
      System.out.println(broadcastMessage.deserialize(dataInputStream));
      responseMsgStr = broadcast(broadcastMessage);
    }

    if (responseMsgStr != null) {
      Message responseMsg = messageHandler.createFail(responseMsgStr);
      send(responseMsg);
    }
  }

  /**
   * Broadcast message to all clients and generate a result message
   * @param boardcastMessage  the message to be broadcast
   * @return  the message
   */
  private String broadcast(Message boardcastMessage) {
    if (!usersSocketManager.contains(clientName)) {
      return "send failed, the sender username is invalid";
    }
    Iterator iterator = usersSocketManager.queryAll().iterator();
    while (iterator.hasNext()) {
      send((String) iterator.next(), boardcastMessage);
    }
    return null;
  }

  /**
   * Send back a response to client generate a result message
   * @param Message the message
   * @return  true if the message send
   */
  private boolean send(Message Message) {
    System.out.println(SERVER_NAME + " sending a response to " + clientName);
    System.out.println(Message.toString());

    byte[] byteMessage = Message.serialize();
    try {
      this.outputStream.write(byteMessage);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return true;
  }

  /**
   * Send a message to another clients generate a result message
   * @param recipient the recipient client
   * @param sendMsg the send message
   * @return  the message send if true
   */
  private String send(String recipient, Message sendMsg) {

    if (!usersSocketManager.contains(clientName)) {
      return "send failed, the sender username is invalid";
    }
    if (!usersSocketManager.contains(recipient)) {
      return "send failed, did not find recipient " + recipient;
    }
    byte[] byteMessage = sendMsg.serialize();
    try {
      OutputStream recipientOutputStream = usersSocketManager.getSocket(recipient)
          .getOutputStream();
      recipientOutputStream.write(byteMessage);
      recipientOutputStream.flush();
      System.out.println(SERVER_NAME + " sending a message to " + recipient);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return null;
  }

  /**
   * Send an insult to another client generate a result message
   * @param insultMsg the insult message to be sent
   * @return  the message if is true
   */
  private String sendInsult(InsultMessage insultMsg) {
    String insultStr = RandomInsultGenerator.generate();
    System.out.println(SERVER_NAME + " generate Insult: " + insultStr);
    insultMsg.setMessage(insultStr);
    return send(insultMsg.getRecipientUsername(), insultMsg);
  }
}