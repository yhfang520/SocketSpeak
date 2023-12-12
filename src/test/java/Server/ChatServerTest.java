package Server;

import Client.MockServer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;

public class ChatServerTest {
  private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
  private final  InputStream inputStream = System.in;
  private MockServer mockServer;
  private MockClient mockClient;
  private final String mainExpectedOutput = "start listening on 5010\n";
  private final String startExpectedOutput = "start listening on 5010\n";
  @BeforeEach
  void setUp() {
    System.setOut(new PrintStream(outputStream));
    mockServer = new MockServer(5010);
    mockServer.start();

    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
    mockClient = new MockClient(5010, "Anna");
  }

  @AfterEach
  void tearDown() {
    System.setIn(inputStream);
    System.setOut(System.out);
    mockClient.stop();
    mockServer.stop();
  }

  @Test
  void mainTest() {
    String[] args = {"5010"};
    System.setIn(new ByteArrayInputStream("quit".getBytes()));
    new Thread(() -> ChatServer.startServer(5010)).start();
    try {
      Thread.sleep(1000);
    } catch(InterruptedException e) {
      throw new RuntimeException(e);
    }
    ChatServer.main(args);
    Assertions.assertTrue(outputStream.toString().contains(mainExpectedOutput));
  }

  @Test
  void startServerTest() {
    String[] args = {"5010"};
    System.setIn(new ByteArrayInputStream("quit".getBytes()));
    ChatServer.main(args);
    Assertions.assertTrue(outputStream.toString().contains(startExpectedOutput));
  }

  @Test
  void mainMultipleConnectTest() {
    String[] args = {"5200"};
    new Thread(() -> ChatServer.main(args)).start();
    try {
      Thread.sleep(1000);
    } catch(InterruptedException e) {
      throw new RuntimeException(e);
    }
    MockClient test = new MockClient(5200, "Bob");
    MockClient test2 = new MockClient(5200, "Alice");
    MockClient test3 = new MockClient(5200, "Bella");
    try {
      Thread.sleep(1000);
    } catch(InterruptedException e) {
      throw new RuntimeException(e);
    }
    test.stop();
    test2.stop();
    test3.stop();
    Assertions.assertTrue(outputStream.toString().contains("start listening on 5200"));
  }
}