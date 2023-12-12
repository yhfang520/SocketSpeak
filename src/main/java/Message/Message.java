package Message;

import java.io.DataInputStream;

/**
 * Represents the Message
 */
public interface Message {

  /**
   * Deserialize the BroadcastMessage from the DataInputStream
   * @param dataInputStream the input stream
   * @return deserialize username
   */
  public String deserialize(DataInputStream dataInputStream);

  /**
   * Serialize the BroadcastMessage to the byte array
   * @return byte array
   */
  public byte[] serialize();

  /**
   * The toString will be called to display messages in console and test
   * @return  string
   */
  public String toString();

  /**
   * Get the identifier
   * @return the identifier
   */
  public int getIdentifier();
}
