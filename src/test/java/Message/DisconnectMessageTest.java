package Message;

import static org.junit.jupiter.api.Assertions.*;
import static Message.MessageHardcode.Identifier.DISCONNECT_MESSAGE;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DisconnectMessageTest {

  private Message message;
  private static final String username = "name";

  @BeforeEach
  void setUp() {
    MessageHandler messageHandler = new MessageHandler();
    message = messageHandler.create(DISCONNECT_MESSAGE,"Anne");
  }

  @Test
  void constructorTest() {
    DisconnectMessage disconnectMessage = new DisconnectMessage();
    Assertions.assertEquals(DISCONNECT_MESSAGE, disconnectMessage.getIdentifier());
    Assertions.assertNull(disconnectMessage.getUsername());
  }

  @Test
  void serialize() {
    DisconnectMessage disconnectMessage = (DisconnectMessage) message;
    byte[] byteMsg =  message.serialize();
    DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(byteMsg));
    try {
      dataInputStream.readInt();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    String messageStr = disconnectMessage.deserialize(dataInputStream);
    Assertions.assertEquals("(Disconnect message) Anne is disconnecting",messageStr);
  }

  @Test
  void getIdentifier() {
    Assertions.assertEquals(DISCONNECT_MESSAGE,message.getIdentifier());
  }

  @Test
  void getUsernameTest() {
    DisconnectMessage disconnectMessage = (DisconnectMessage) message;
    Assertions.assertEquals("Anne", disconnectMessage.getUsername());
  }

  @Test
  void hashCodeTest() {
    DisconnectMessage test = new DisconnectMessage(username);
    DisconnectMessage test2 = new DisconnectMessage(username);
    Assertions.assertEquals(test.hashCode(), test2.hashCode());
  }

  @Test
  void toStringTest() {
    DisconnectMessage disconnectMessage = (DisconnectMessage) message;
    Assertions.assertEquals("(Disconnect message) Anne is disconnecting",disconnectMessage.toString());
  }
}