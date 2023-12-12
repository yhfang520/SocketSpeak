package Message;

import static org.junit.jupiter.api.Assertions.*;
import static Message.MessageHardcode.Identifier.QUERY_USER_RESPONSE;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class QueryUsersResponseTest {
  Message message;

  @BeforeEach
  void setUp() {
    MessageHandler messageHandler = new MessageHandler();
    ArrayList<String> usernames = new ArrayList<>();
    usernames.add("Anne");
    usernames.add("Bob");
    usernames.add("Tom");
    message  =  messageHandler.createResponse(usernames);

  }
  @Test
  void serializeAndDeserialize() {
    byte[] byteMsg =  message.serialize();
    DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(byteMsg));
    try {
      dataInputStream.readInt();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    QueryUsersResponse queryUsersResponse = new QueryUsersResponse();
    queryUsersResponse.deserialize(dataInputStream);
    Assertions.assertEquals("(Query Response) 3 users found:\nAnne\nBob\nTom",queryUsersResponse.toString());
  }

  @Test
  void serializeAndDeserialize2() {
    QueryUsersResponse queryUsersResponse = new QueryUsersResponse();
    DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(new byte[0]));
    Assertions.assertThrows(RuntimeException.class, () -> queryUsersResponse.deserialize(dataInputStream));
  }

  @Test
  void getIdentifierTest() {
    Assertions.assertEquals(QUERY_USER_RESPONSE, message.getIdentifier());
  }

  @Test
  void equalsTest() {
    ArrayList<String> username = new ArrayList<>(Arrays.asList("name1", "name2", "name3"));
    QueryUsersResponse test = new QueryUsersResponse(username);
    QueryUsersResponse test2 = new QueryUsersResponse(username);
    Assertions.assertTrue(test.equals(test));
    Assertions.assertTrue(test.equals(test2));
    Assertions.assertFalse(test.equals(null));
    Assertions.assertFalse(test.equals(new String()));
  }

  @Test
  void hashCodeTest() {
    ArrayList<String> username = new ArrayList<>(Arrays.asList("name1", "name2", "name3"));
    QueryUsersResponse test = new QueryUsersResponse(username);
    QueryUsersResponse test2 = new QueryUsersResponse(username);
    Assertions.assertEquals(test.hashCode(), test2.hashCode());
  }
}