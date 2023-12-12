package Message;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static Message.MessageHardcode.Identifier.CONNECT_MESSAGE;

class ConnectMessageTest {
  Message message;
  private static final String username = "name";

  @BeforeEach
  void setUp() {
    MessageHandler messageHandler = new MessageHandler();
    message  =  messageHandler.create(CONNECT_MESSAGE,"Anne");
  }

  @Test
  void constructorTest() {
    ConnectMessage connectMessage = new ConnectMessage();
    Assertions.assertEquals(CONNECT_MESSAGE, connectMessage.getIdentifier());
    Assertions.assertNull(connectMessage.getUsername());
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
    Assertions.assertEquals("(Connect message) Anne is connecting to",messageStr);
  }

  @Test
  void testSerializeAndDeserialize2() {
    ConnectMessage connectMessage = new ConnectMessage();
    DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(new byte[0]));
    Assertions.assertThrows(RuntimeException.class, () -> connectMessage.deserialize(dataInputStream));
  }

  @Test
  void getUsernameTest() {
    Assertions.assertEquals("Anne", ((ConnectMessage) message).getUsername());
  }
  
  @Test
  void setIdentifierTest() {
    ConnectMessage connectMessage = (ConnectMessage) message;
    connectMessage.setIdentifier(77);
    Assertions.assertEquals(77, connectMessage.getIdentifier());
  }

  @Test
  void equalsTest() {
    ConnectMessage test = new ConnectMessage(username);
    ConnectMessage test2 = new ConnectMessage(username);
    ConnectMessage test3 = new ConnectMessage("name2");
    Assertions.assertTrue(test.equals(test));
    Assertions.assertTrue(test.equals(test2));
    Assertions.assertFalse(test.equals(test3));
    Assertions.assertFalse(test.equals(null));
    Assertions.assertFalse(test.equals(new String()));
  }

  @Test
  void hashCodeTest() {
    ConnectMessage test = new ConnectMessage(username);
    ConnectMessage test2 = new ConnectMessage(username);
    Assertions.assertEquals(test.hashCode(), test2.hashCode());
  }

  @Test
  void toStringTest() {
    Assertions.assertEquals("(Connect message) Anne is connecting to",message.toString());
  }
}