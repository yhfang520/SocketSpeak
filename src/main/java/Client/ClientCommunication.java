package Client;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.regex.Matcher;

import Message.*;


import static Client.UserCommands.INSULT;
import static Client.UserCommands.INSULT_SYMBOL;
import static Client.UserCommands.LOGOFF;
import static Client.UserCommands.WHO;
import static Message.MessageHardcode.Identifier.CONNECT_MESSAGE;

import static Client.UserCommands.HELP;
import static Client.UserCommands.COMMAND_PATTERNS;

import static Client.UserCommands.DIRECT;
import static Client.UserCommands.DIRECT_SYMBOL;
import static Client.UserCommands.BROADCAST;
import static Message.MessageHardcode.Identifier.*;

/**
 * Represents the client side communication each user will create and launch a client communication thread
 * to communicate with server using socket
 */
public class ClientCommunication implements Runnable {

  private Socket clientSocket;
  private String username;
  private InputStream inputStream;
  private OutputStream outputStream;
  private DataInputStream dataInputStream;
  private MessageHandler messageHandler;
  private static String CLIENT_CHAT_NAME;
  private boolean connected = false;

  /**
   * Constructor for ClientCommunication, create a client communication thread with client socket
   *
   * @param clientSocket    the socket for communication with server
   * @param username        the client's username
   */
  public ClientCommunication(Socket clientSocket, String username) {
    this.clientSocket = clientSocket;
    this.username = username;
    CLIENT_CHAT_NAME = String.format("[%s Chat]", username);
    this.messageHandler = new MessageHandler();
    if (clientSocket != null) {
      try {
        this.inputStream = this.clientSocket.getInputStream();
        this.outputStream = this.clientSocket.getOutputStream();
        this.dataInputStream = new DataInputStream(this.inputStream);

      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

  /**
   * If connect is successful continue handle user commands util disconnect
   */
  @Override
  public void run() {
    connect();
    if (connected) {
      System.out.println("connection start");
      keepCommunicating(new Scanner(System.in));
    }
    System.out.println("connection stop");
  }

  /**
   * Check is connected
   * @return if connected
   */
  public boolean isConnected() {
    return connected;
  }

  /**
   * Connect to server and wait for a connect response. If connect is successful, modify connected state to true,
   * then start handle further user input and messages. Otherwise chat thread quit
   */
  private void connect() {
    Message connectMessage = messageHandler.create(CONNECT_MESSAGE, this.username);
    send(connectMessage);
    waitUntilResponse();
    handleNewMessages();
  }


  /**
   * Continue handle user commands and messages util disconnect
   * @param scanner the scanner
   */
  public void keepCommunicating(Scanner scanner) {
    if(scanner==null) {scanner = new Scanner(System.in);}
    try {
      while (connected) {
        if (System.in.available() > 0) {
          String userInput = scanner.nextLine().trim();
          Message sendMessage = parseCommand(userInput);
          if (sendMessage!=null){
            send(sendMessage);
          }
        }
        handleNewMessages();
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }finally {
      scanner.close();
    }
  }

  /**
   * Check and parse valid userInput into protocol message
   * @param userInput the user's input
   * @return  the parsed message
   */
  public Message parseCommand(String userInput) {
    if (userInput.equals(HELP)) {
      System.out.println("match command " + HELP);
      showAllUserCommands();
      return null;
    }

    Matcher matcher = COMMAND_PATTERNS.matcher(userInput);

    if (!matcher.find()) {
      reportUnknownMessageError(userInput);
      return null;
    }

    Message message = null;
    String command = matcher.group();
    int restIndex = matcher.end();
    if (command.matches(LOGOFF)) {
      message = messageHandler.create(DISCONNECT_MESSAGE, username);
    }
    if (command.matches(WHO)) {
      message = messageHandler.create(QUERY_CONNECTED_USERS, username);
    }
    if (command.matches(DIRECT)) {
      String recipientName = command.replace(DIRECT_SYMBOL, "");
      String messageStr = userInput.substring(restIndex).trim();
      message = messageHandler.create(DIRECT_MESSAGE, username, recipientName, messageStr);
    }
    if (command.matches(INSULT)) {
      String recipientName = command.replace(INSULT_SYMBOL, "");
      String messageStr = userInput.substring(restIndex).trim();
      message = messageHandler.create(SEND_INSULT, username, recipientName, messageStr);
    }
    if (command.matches(BROADCAST)) {
      String messageStr = userInput.substring(restIndex).trim();
      message = messageHandler.create(username, messageStr);
    }
    return message;
  }

  /**
   * Check if there are new messages or responses from server handle received messages from server
   * modify connected state given connect response
   */
  private void handleNewMessages() {
    try {
      while (dataInputStream.available() > 0) {
        System.out.println(CLIENT_CHAT_NAME + " receive a response:");

        int responseId = dataInputStream.readInt();
        Message response = messageHandler.create(responseId);
        System.out.println(response.deserialize(dataInputStream));
        shouldModifyConnect(responseId, response);
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Wait until receive a response
   */
  private void waitUntilResponse() {

    try {
      while (true) {
        if (dataInputStream.available() > 0) {
          break;
        }
      }

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Modify connected state given connect response.
   * If not connected and connected response is true, connected state = true
   * If connected and disconnected response is true, connected state = false
   * @param responseId  the identifier of the response
   * @param response  the response message
   */
  public void shouldModifyConnect(int responseId, Message response) {
    if (responseId == CONNECT_RESPONSE) {
      if (!connected) {
        this.connected = ((ConnectResponse) response).isSuccess();
      } else {
        this.connected = !((ConnectResponse) response).isSuccess();
      }
    }
  }

  /**
   * Send message to connected chat server
   * @param message the send message
   */
  private void send(Message message) {
    byte[] byteMessage = message.serialize();
    System.out.println(message.toString());
    try {
      this.outputStream.write(byteMessage);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    System.out.println(CLIENT_CHAT_NAME + " message is sending");
  }

  /**
   * Display the commands menu
   */
  private void showAllUserCommands() {
    System.out.println("Commands menu: ");
    System.out.println("?: display the commands menu");
    System.out.println("logoff: log off");
    System.out.println("who: query connected users");
    System.out.println("@user: sends a DIRECT_MESSAGE to the specified user");
    System.out.println("@all: sends a BROADCAST_MESSAGE to all users");
    System.out.println("!user: sends a SEND_INSULT message to the specified user");
  }

  /**
   * Report unknown message error
   * @param userInput the users input
   */
  private void reportUnknownMessageError(String userInput) {
    System.out.println(String.format("\"%s\" is not valid command, please use following commands",userInput));
    showAllUserCommands();
    }
}
