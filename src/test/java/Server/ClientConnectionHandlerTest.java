package Server;

import static org.junit.jupiter.api.Assertions.*;

import Client.ClientCommunication;
import Message.BroadcastMessage;
import Message.ConnectMessage;
import Message.DirectMessage;
import Message.DisconnectMessage;
import Message.InsultMessage;
import Message.QueryUsersMessage;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ClientConnectionHandlerTest {

  private Thread serverThread;
  private UsersSocketManager usersSocketManager = new UsersSocketManager(new ConcurrentHashMap<>());
  private ClientConnectionHandler clientConnectionHandler;
  private ServerSocket serverSocket;
  private Socket newClientSocket;
  private Socket clientSocket;
  private ByteArrayOutputStream serverOutputStream;
  private DataOutputStream dataOutputStream;


  @BeforeEach
  void setUp() throws IOException {
    if (serverSocket == null || serverSocket.isClosed()) {
      try {
        serverSocket = new ServerSocket(5030);
        clientSocket = new Socket("localhost", 5030);
        newClientSocket = serverSocket.accept();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
      clientConnectionHandler = new ClientConnectionHandler(newClientSocket, usersSocketManager);

      serverOutputStream = new ByteArrayOutputStream();
      dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
    }
  }


  @Test
  void testSendDirect() throws IOException, InterruptedException {
    dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());

    ConnectMessage connectMessage = new ConnectMessage("Anne");
    dataOutputStream.write(connectMessage.serialize());
    Assertions.assertTrue(clientConnectionHandler.connect());

    serverThread = new Thread(() -> {
      System.setOut(new PrintStream(serverOutputStream));
      clientConnectionHandler.handleMessages();
    });
    serverThread.start();

    dataOutputStream.write(new DirectMessage("Anne", "Anne", "hello").serialize());

    Thread.sleep(1000);
    String output = serverOutputStream.toString().trim();
    String expected = "[Server] received a message:\n"
        + "(Direct Message) Anne send to Anne: hello\n"
        + "[Server] sending a message to Anne";
    Assertions.assertEquals(expected, output);

    closeAll();
  }


  @Test
  void testBroadcast() throws IOException, InterruptedException {
    dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());

    ConnectMessage connectMessage = new ConnectMessage("Anne");
    dataOutputStream.write(connectMessage.serialize());
    Assertions.assertTrue(clientConnectionHandler.connect());

    serverThread = new Thread(() -> {
      System.setOut(new PrintStream(serverOutputStream));
      clientConnectionHandler.handleMessages();
    });
    serverThread.start();

    dataOutputStream.write(new BroadcastMessage("Anne", "hello").serialize());

    Thread.sleep(1000);
    String output = serverOutputStream.toString().trim();
    String expected = "[Server] received a message:\n"
        + "(Broadcast Message) Anne send to All: hello\n"
        + "[Server] sending a message to Anne";
    Assertions.assertEquals(expected, output);

    closeAll();
  }


  @Test
  void testQuery() throws IOException, InterruptedException {
    dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());

    ConnectMessage connectMessage = new ConnectMessage("Anne");
    dataOutputStream.write(connectMessage.serialize());
    Assertions.assertTrue(clientConnectionHandler.connect());

    serverThread = new Thread(() -> {
      System.setOut(new PrintStream(serverOutputStream));
      clientConnectionHandler.handleMessages();
    });
    serverThread.start();

    dataOutputStream.write(new QueryUsersMessage("Anne").serialize());

    Thread.sleep(1000);
    String output = serverOutputStream.toString().trim();
    String expected = "[Server] received a message:\n"
        + "(Query message) Anne query all connected users\n"
        + "[Server] sending a response to Anne\n"
        + "(Query Response) 1 users found:\n"
        + "Anne";
    Assertions.assertEquals(expected, output);

    closeAll();
  }

  @Test
  void testInsult() throws IOException, InterruptedException {
    dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());

    ConnectMessage connectMessage = new ConnectMessage("Anne");
    dataOutputStream.write(connectMessage.serialize());
    Assertions.assertTrue(clientConnectionHandler.connect());

    serverThread = new Thread(() -> {
      System.setOut(new PrintStream(serverOutputStream));
      clientConnectionHandler.handleMessages();
    });
    serverThread.start();

    dataOutputStream.write(new InsultMessage("Anne","Anne","").serialize());

    Thread.sleep(1000);
    String output = serverOutputStream.toString().trim();
    Assertions.assertTrue(output.contains("[Server] generate Insult:"));

    closeAll();
  }

  @Test
  void testDisConnect() throws IOException, InterruptedException {
    dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());

    ConnectMessage connectMessage = new ConnectMessage("Anne");
    dataOutputStream.write(connectMessage.serialize());
    Assertions.assertTrue(clientConnectionHandler.connect());

    serverThread = new Thread(() -> {
      System.setOut(new PrintStream(serverOutputStream));
      clientConnectionHandler.handleMessages();
    });
    serverThread.start();

    dataOutputStream.write(new DisconnectMessage("Anne").serialize());

    Thread.sleep(1000);
    String output = serverOutputStream.toString().trim();
    String expected = "[Server] received a message:\n"
        + "(Disconnect message) Anne is disconnecting\n"
        + "[Server] sending a response to Anne\n"
        + "connection response: success is true, You are no longer connected";
    Assertions.assertEquals(expected, output);

    closeAll();
  }



  @Test
  void connectAndDisConnect() throws IOException {

    ConnectMessage connectMessage = new ConnectMessage("Anne");
    dataOutputStream.write(connectMessage.serialize());
    Assertions.assertTrue(clientConnectionHandler.connect());

    connectMessage = new ConnectMessage("Bob");
    dataOutputStream.write(connectMessage.serialize());
    Assertions.assertTrue(clientConnectionHandler.connect());

    connectMessage = new ConnectMessage("Anne");
    dataOutputStream.write(connectMessage.serialize());
    Assertions.assertFalse(clientConnectionHandler.connect());

    DisconnectMessage disconnectMessage = new DisconnectMessage("Tom");
    dataOutputStream.write(disconnectMessage.serialize());
    Assertions.assertFalse(clientConnectionHandler.connect());

    serverSocket.close();
  }

  void closeAll() throws IOException {
    serverThread.interrupt();
    serverSocket.close();
    System.setOut(System.out);
  }

}