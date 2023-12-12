package Message;

import static Message.MessageHardcode.Identifier.DIRECT_MESSAGE;
import static Message.MessageHardcode.DELIMITER;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DirectMessageTest {
  Message message;
  private static final String senderUsername = "sender name";
  private static final String recipientUsername = "recipient name";
  private static final String directMessage = "message";

  @BeforeEach
  void setUp() {
    MessageHandler messageHandler = new MessageHandler();
    message  =  messageHandler.create(25,"Anne","Bob","hello!");
  }

  @Test
  void constructorTest() {
    DirectMessage directMessage = new DirectMessage();
    Assertions.assertEquals(DIRECT_MESSAGE, directMessage.getIdentifier());
    Assertions.assertNull(directMessage.getUsername());
  }

  @Test
  void testSerializeAndDeserialize() {
    byte[] byteMsg =  message.serialize();
    DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(byteMsg));
    try {
      dataInputStream.readInt();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    String messageStr = message.deserialize(dataInputStream);
    Assertions.assertEquals("(Direct Message) Anne send to Bob: hello!",messageStr);
  }

  @Test
  void testSerializeAndDeserialize2() {
    DirectMessage directMessage = new DirectMessage();
    DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(new byte[0]));
    Assertions.assertThrows(RuntimeException.class, () -> directMessage.deserialize(dataInputStream));
    Assertions.assertThrows(RuntimeException.class, () -> directMessage.serializeSequential());
  }

  @Test
  void equalsTest() {
    DirectMessage test = new DirectMessage(senderUsername, recipientUsername, directMessage);
    DirectMessage test2 = new DirectMessage(senderUsername, recipientUsername, directMessage);
    DirectMessage test3 = new DirectMessage("name2", recipientUsername, directMessage);
    DirectMessage test4 = new DirectMessage(senderUsername, "name3", directMessage);
    DirectMessage test5 = new DirectMessage(senderUsername, recipientUsername, "message3");
    Assertions.assertTrue(test.equals(test));
    Assertions.assertTrue(test.equals(test2));
    Assertions.assertFalse(test.equals(test3));
    Assertions.assertFalse(test.equals(test4));
    Assertions.assertFalse(test.equals(test5));
    Assertions.assertFalse(test.equals(null));
    Assertions.assertFalse(test.equals(new String()));
  }

  @Test
  void hashCodeTest() {
    DirectMessage test = new DirectMessage(senderUsername, recipientUsername, directMessage);
    DirectMessage test2 = new DirectMessage(senderUsername, recipientUsername, directMessage);
    Assertions.assertEquals(test.hashCode(), test2.hashCode());
  }
}

