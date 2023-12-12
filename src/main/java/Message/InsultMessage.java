package Message;

import static Message.MessageHardcode.Identifier.SEND_INSULT;
import java.io.DataInputStream;
import java.util.Objects;

/**
 * Represents the Insult Message
 */
public class InsultMessage extends DirectMessage{
//  private int identifier;
//  private String senderUsername;
//  private String recipientUsername;
//  private String message;

  /**
   * Constructor for InsultMessage
   */
  public InsultMessage() {
    setIdentifier(SEND_INSULT);
  }

  /**
   * Constructor for InsultMessage
   *
   * @param senderUsername      the sender username
   * @param recipientUsername   the recipient username
   * @param message             the message
   */
  public InsultMessage(String senderUsername, String recipientUsername,String message) {
    super(senderUsername,recipientUsername,message);
    setIdentifier(SEND_INSULT);
  }

  /**
   * Serialize the InsultMessage to the byte array
   * @return byte array
   */
  @Override
  public byte[] serialize() {
    return super.serialize();
  }

  /**
   * Deserialize the InsultMessage from the DataInputStream
   * @param dataInputStream the input stream
   * @return deserialize username
   */
  @Override
  public String deserialize(DataInputStream dataInputStream) {
    return super.deserialize(dataInputStream);
  }

  /**
   * Get the user's name
   * @return the user's name
   */
  @Override
  public String getUsername() {
    return super.getUsername();
  }

  /**
   * Get the identifier
   * @return the identifier
   */
  @Override
  public int getIdentifier() {
    return super.getIdentifier();
  }

  /**
   * Get the message
   * @param message the message
   */
  @Override
  public void setMessage(String message) {
    super.setMessage(message);
  }

  /**
   * Hash code of the InsultMessage object
   * @return hash code
   */
  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode());
  }

  /**
   * To string show all the InsultMessage's info
   * @return string
   */
  @Override
  public String toString() {
    return String.format("(Insult Message) %s send to %s: %s", getUsername(),getRecipientUsername(),getMessage());
  }
}
