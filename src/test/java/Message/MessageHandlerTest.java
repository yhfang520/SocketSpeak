package Message;

import static Message.MessageHardcode.Identifier.*;
import static Message.MessageHardcode.DELIMITER;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MessageHandlerTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void createTest() {
        MessageHandler messageHandler = new MessageHandler();
        int[] identifiers = {
                CONNECT_MESSAGE,
                DISCONNECT_MESSAGE,
                QUERY_CONNECTED_USERS,
                DIRECT_MESSAGE,
                BROADCAST_MESSAGE,
                SEND_INSULT,
                CONNECT_RESPONSE,
                QUERY_USER_RESPONSE,
                FAILED_MESSAGE
        };
        for(int identifier : identifiers) {
            Message message = messageHandler.create(identifier);
            switch(identifier) {
                case CONNECT_MESSAGE:
                    Assertions.assertTrue(message instanceof ConnectMessage);
                    break;
                case DISCONNECT_MESSAGE:
                    Assertions.assertTrue(message instanceof DisconnectMessage);
                    break;
                case QUERY_CONNECTED_USERS:
                    Assertions.assertTrue(message instanceof QueryUsersMessage);
                    break;
                case DIRECT_MESSAGE:
                    Assertions.assertTrue(message instanceof DirectMessage);
                    break;
                case BROADCAST_MESSAGE:
                    Assertions.assertTrue(message instanceof BroadcastMessage);
                    break;
                case SEND_INSULT:
                    Assertions.assertTrue(message instanceof InsultMessage);
                    break;
                case CONNECT_RESPONSE:
                    Assertions.assertTrue(message instanceof ConnectResponse);
                    break;
                case QUERY_USER_RESPONSE:
                    Assertions.assertTrue(message instanceof QueryUsersResponse);
                    break;
                case FAILED_MESSAGE:
                    Assertions.assertTrue(message instanceof FailMessage);
                    break;
                default:
                    Assertions.fail("Unknow identifier: " + identifier);
            }
        }
    }

    @Test
    void serialize() {
        String sender = "Anne";
        String recipient = "Bob";
        String message = "Hello!";

        MessageHandler messageHandler = new MessageHandler();
        Message directmessage  =  messageHandler.create(25,sender,recipient,message);
        byte[] expectedByteMsg =  ((DirectMessage)directmessage).serializeSequential();

        byte[] byteMsg = messageHandler.join(null,25);
        byteMsg = messageHandler.join(byteMsg,sender,recipient,message);

        Assertions.assertArrayEquals(expectedByteMsg,byteMsg);
    }
}