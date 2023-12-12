package Message;

import static org.junit.jupiter.api.Assertions.*;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static Message.MessageHardcode.Identifier.QUERY_CONNECTED_USERS;

class QueryUsersMessageTest {

  private Message message;
  private static final String username = "name";

  @BeforeEach
  void setUp() {
    MessageHandler messageHandler = new MessageHandler();
    message = messageHandler.create(QUERY_CONNECTED_USERS,"Anne");
  }

  @Test
  void isRightId() {
    Assertions.assertEquals(QUERY_CONNECTED_USERS,message.getIdentifier());
  }

  @Test
  void serialize() {
    byte[] serializedMessage = message.serialize();
    Assertions.assertNotNull(serializedMessage);
  }

  @Test
  void deserialize() {
    byte[] byteMsg =  message.serialize();
    DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(byteMsg));
    try {
      dataInputStream.readInt();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    String messageStr = message.deserialize(dataInputStream);
    Assertions.assertEquals("(Query message) Anne query all connected users",messageStr);
  }

  @Test
  void toStringTest() {
    Assertions.assertEquals("(Query message) Anne query all connected users",message.toString());
  }

  @Test
  void getUsernameTest() {
    Assertions.assertEquals("Anne", ((QueryUsersMessage) message).getUsername());
  }

  @Test
  void hashCodeTest() {
    QueryUsersMessage test = new QueryUsersMessage(username);
    QueryUsersMessage test2 = new QueryUsersMessage(username);
    Assertions.assertEquals(test.hashCode(), test2.hashCode());
  }
}