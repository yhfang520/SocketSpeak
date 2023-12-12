package Client;

import static Message.MessageHardcode.Identifier.CONNECT_RESPONSE;

import Message.Message;
import Message.ConnectResponse;
import Server.ClientConnectionHandler;
import Server.UsersSocketManager;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ClientCommunicationTest {

  private Thread serverThread;
  private Thread clientThread;
  private UsersSocketManager usersSocketManager = new UsersSocketManager(new ConcurrentHashMap<>());
  private ClientConnectionHandler clientConnectionHandler;
  private ClientCommunication clientCommunication;
  private ServerSocket serverSocket;
  private Socket newClientSocket;
  private Socket clientSocket;
  private ByteArrayOutputStream clientOutputStream;

  @BeforeEach
  void setUP() {
      try {
        serverSocket = new ServerSocket(5020);
        clientSocket = new Socket("localhost", 5020);
        newClientSocket = serverSocket.accept();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
      clientConnectionHandler = new ClientConnectionHandler(newClientSocket, usersSocketManager);
      clientCommunication = new ClientCommunication(clientSocket, "Anne");

      clientOutputStream = new ByteArrayOutputStream();
  }

  @Test
  void connectAndDisConnect() throws InterruptedException, IOException {

    Assertions.assertFalse(clientCommunication.isConnected());

    serverThread = new Thread(() -> {
      clientConnectionHandler.run();
    });
    clientThread = new Thread(() -> {
      System.setOut(new PrintStream(clientOutputStream));
      clientCommunication.run();
    });

    serverThread.start();
    clientThread.start();
    Thread.sleep(1000);


    Assertions.assertTrue(clientCommunication.isConnected());
    String output = clientOutputStream.toString().trim();
    String expected = "(Connect message) Anne is connecting to\n"
        + "[Anne Chat] message is sending\n"
        + "(Connect message) Anne is connecting to\n"
        + "[Server] sending a response to Anne\n"
        + "connection response: success is true, Connect was successful, there are 1 other connected clients\n"
        + "[Anne Chat] receive a response:\n"
        + "connection response: success is true, Connect was successful, there are 1 other connected clients\n"
        + "connection start";
    Assertions.assertEquals(expected, output);


    ConnectResponse disconnect = new ConnectResponse(true,"");
    clientCommunication.shouldModifyConnect(CONNECT_RESPONSE,disconnect);
    Assertions.assertFalse(clientCommunication.isConnected());


    closeAll();

  }

  @Test
  void testParseCommand() throws IOException {

    Message message = clientCommunication.parseCommand("@Anne hello!");
    Assertions.assertEquals("(Direct Message) Anne send to Anne: hello!", message.toString());

    message = clientCommunication.parseCommand("@all hello!");
    Assertions.assertEquals("(Broadcast Message) Anne send to All: hello!", message.toString());

    message = clientCommunication.parseCommand("!Anne hello!");
    Assertions.assertTrue(message.toString().contains("(Insult Message) Anne send to Anne:"));

    message = clientCommunication.parseCommand("who");
    Assertions.assertEquals("(Query message) Anne query all connected users", message.toString());

    message = clientCommunication.parseCommand("logoff");
    Assertions.assertEquals("(Disconnect message) Anne is disconnecting", message.toString());

    closeSocket();

    message = clientCommunication.parseCommand("?");
    Assertions.assertNull(message);
    message = clientCommunication.parseCommand("invalid");
    Assertions.assertNull(message);


  }

  @Test
  void keepCommunicating() throws IOException, InterruptedException {
    serverThread = new Thread(() -> {
      clientConnectionHandler.run();
    });
    clientThread = new Thread(() -> {
      clientCommunication.run();
    });

    serverThread.start();
    clientThread.start();

    Thread.sleep(1000);
    Assertions.assertTrue(clientCommunication.isConnected());

    closeAll();
  }

  void closeAll() throws IOException {
    clientThread.interrupt();
    serverThread.interrupt();
    serverSocket.close();
  }

  void closeSocket() throws IOException {
    serverSocket.close();
  }

}
