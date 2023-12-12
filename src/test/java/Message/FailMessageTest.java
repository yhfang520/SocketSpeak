package Message;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static Message.MessageHardcode.Identifier.FAILED_MESSAGE;

class FailMessageTest {
  Message message;
  private static final String fMessage = "message";

  @BeforeEach
  void setUp() {
    MessageHandler messageHandler = new MessageHandler();
    message = messageHandler.createFail("Fail message");

  }

  @Test
  void constructorTest() {
    FailMessage failMessage = new FailMessage();
    Assertions.assertEquals(FAILED_MESSAGE, failMessage.getIdentifier());
    Assertions.assertNull(failMessage.getMessage());
  }

  @Test
  void testSerializeAndDeserialize() {
    MessageHandler messageHandler = new MessageHandler();
    message  =  messageHandler.createFail("connect Fail");
    byte[] byteMsg =  message.serialize();
    DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(byteMsg));
    try {
      dataInputStream.readInt();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    String messageStr = message.deserialize(dataInputStream);
    Assertions.assertEquals("(Fail message) connect Fail",messageStr);
  }

  @Test
  void equalsTest() {
    FailMessage test = new FailMessage(fMessage);
    FailMessage test2 = new FailMessage(fMessage);
    Assertions.assertTrue(test.equals(test));
    Assertions.assertTrue(test.equals(test2));
  }

  @Test
  void hashCodeTest() {
    FailMessage test = new FailMessage(fMessage);
    FailMessage test2 = new FailMessage(fMessage);
    Assertions.assertEquals(test.hashCode(), test2.hashCode());
  }
}