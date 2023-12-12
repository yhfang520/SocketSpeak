package Client;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;

public class ChatClientTest {
  private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
  private final  InputStream inputStream = System.in;
  private MockServer mockServer;
  private final String expectedOutput = "Anna is connect to localhost 1234\n";

  @BeforeEach
  void setUp() {
    System.setOut(new PrintStream(outputStream));
    mockServer = new MockServer(1234);
    mockServer.start();
  }

  @AfterEach
  void tearDown() {
    System.setIn(inputStream);
    System.setOut(System.out);
    mockServer.stop();
    try {
      outputStream.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  void mainTest() {
    String[] args = {"1234", "Anna"};
    ChatClient.main(args);
    Assertions.assertTrue(outputStream.toString().contains(expectedOutput));
  }
}