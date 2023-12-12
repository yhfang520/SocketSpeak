package Message;

import static Message.MessageHardcode.Identifier.SEND_INSULT;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static Message.MessageHardcode.Identifier.BROADCAST_MESSAGE;

class BroadcastMessageTest {

  Message message;
  private static final int identifier = BROADCAST_MESSAGE;
  private static final String senderUsername = "name";
  private static final String broadMessage = "message";

  @BeforeEach
  void setUp() {
    MessageHandler messageHandler = new MessageHandler();
    message  =  messageHandler.create("Anne","hello!");
  }

  @Test
  void constructorTest() {
    BroadcastMessage broadcastMessage = new BroadcastMessage();
    Assertions.assertEquals(BROADCAST_MESSAGE, broadcastMessage.getIdentifier());
    Assertions.assertNull(broadcastMessage.getUsername());
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
    Assertions.assertEquals("(Broadcast Message) Anne send to All: hello!",messageStr);
  }

  @Test
  void testSerializeAndDeserialize2() {
    BroadcastMessage broadcastMessage = new BroadcastMessage();
    DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(new byte[0]));
    Assertions.assertThrows(RuntimeException.class, () -> broadcastMessage.deserialize(dataInputStream));
  }

  @Test
  void getUsernameTest() {
    Assertions.assertEquals("Anne", ((BroadcastMessage) message).getUsername());
  }

  @Test
  void getIdentifier() {
    Assertions.assertEquals(BROADCAST_MESSAGE,message.getIdentifier());
  }

  @Test
  void equalsTest() {
    BroadcastMessage test = new BroadcastMessage(senderUsername, broadMessage);
    BroadcastMessage test2 = new BroadcastMessage(senderUsername, broadMessage);
    BroadcastMessage test3 = new BroadcastMessage("name2", broadMessage);
    BroadcastMessage test4 = new BroadcastMessage(senderUsername, "broadMessage");
    Assertions.assertTrue(test.equals(test));
    Assertions.assertTrue(test.equals(test2));
    Assertions.assertFalse(test.equals(test3));
    Assertions.assertFalse(test.equals(test4));
    Assertions.assertFalse(test.equals(null));
    Assertions.assertFalse(test.equals(new String()));
  }

  @Test
  void hashCodeTest() {
    BroadcastMessage test = new BroadcastMessage(senderUsername, broadMessage);
    BroadcastMessage test2 = new BroadcastMessage(senderUsername, broadMessage);
    Assertions.assertEquals(test.hashCode(), test2.hashCode());
  }
}