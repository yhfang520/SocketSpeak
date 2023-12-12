package Message;

import static Message.MessageHardcode.Identifier.CONNECT_MESSAGE;
import static Message.MessageHardcode.Identifier.CONNECT_RESPONSE;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ConnectResponseTest {
  Message message;
  private static final boolean success = true;
  private static final String connectMessage = "message";

  @BeforeEach
  void setUp() {
    MessageHandler messageHandler = new MessageHandler();
    message  =  messageHandler.createResponse(false,"connect fail");
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
    Assertions.assertEquals("connection response: success is false, connect fail",messageStr);
  }

  @Test
  void testSerializeAndDeserialize2() {
    ConnectResponse connectResponse = new ConnectResponse();
    DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(new byte[0]));
    Assertions.assertThrows(RuntimeException.class, () -> connectResponse.deserialize(dataInputStream));
  }

  @Test
  void getIdentifierTest() {
    ConnectResponse connectResponse = new ConnectResponse(true, "connect success");
    Assertions.assertEquals(CONNECT_RESPONSE, connectResponse.getIdentifier());
  }

  @Test
  void equalsTest() {
    ConnectResponse test = new ConnectResponse(success, connectMessage);
    ConnectResponse test2 = new ConnectResponse(success, connectMessage);
    ConnectResponse test3 = new ConnectResponse(false, connectMessage);
    ConnectResponse test4 = new ConnectResponse(success, "connectMessage");
    Assertions.assertTrue(test.equals(test));
    Assertions.assertTrue(test.equals(test2));
    Assertions.assertFalse(test.equals(test3));
    Assertions.assertFalse(test.equals(test4));
    Assertions.assertFalse(test.equals(null));
    Assertions.assertFalse(test.equals(new String()));
  }

  @Test
  void hashCodeTest() {
    ConnectResponse test = new ConnectResponse(success, connectMessage);
    ConnectResponse test2 = new ConnectResponse(success, connectMessage);
    Assertions.assertEquals(test.hashCode(), test2.hashCode());
  }
}