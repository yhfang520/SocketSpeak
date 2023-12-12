package Message;

import static org.junit.jupiter.api.Assertions.*;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static Message.MessageHardcode.Identifier.SEND_INSULT;

class InsultMessageTest {

  private Message message;
  private static final String senderUsername = "sender name";
  private static final String recipientUsername = "recipient name";
  private static final String directMessage = "message";

  @BeforeEach
  void setUp() {
    MessageHandler messageHandler = new MessageHandler();
    message = messageHandler.create(SEND_INSULT,"Anne","Bob","");
  }

  @Test
  void constructorTest() {
    InsultMessage insultMessage = new InsultMessage();
    Assertions.assertEquals(SEND_INSULT, insultMessage.getIdentifier());
    Assertions.assertNull(insultMessage.getUsername());
  }

  @Test
  void serialize() {
    byte[] byteMsg =  message.serialize();
    DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(byteMsg));
    try {
      dataInputStream.readInt();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    String messageStr = message.deserialize(dataInputStream);
    Assertions.assertEquals("(Insult Message) Anne send to Bob: ",messageStr);
  }

  @Test
  void getIdentifier() {
    Assertions.assertEquals(SEND_INSULT,message.getIdentifier());
  }

  @Test
  void hashCodeTest() {
    InsultMessage test = new InsultMessage(senderUsername, recipientUsername, directMessage);
    InsultMessage test2 = new InsultMessage(senderUsername, recipientUsername, directMessage);
    Assertions.assertEquals(test.hashCode(), test2.hashCode());
  }
}