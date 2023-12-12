package Message;

/**
 * Represents the Message Hardcode
 */
public class MessageHardcode {

    /**
     * Message identifier for different type of message
     */
    public static class Identifier{

        /**
         * Identifier for CONNECT_MESSAGE
         */
        public final static int CONNECT_MESSAGE = 19;

        /**
         * Identifier for CONNECT_RESPONSE
         */
        public final static int CONNECT_RESPONSE = 20;

        /**
         * Identifier for DISCONNECT_MESSAGE
         */
        public final static int DISCONNECT_MESSAGE = 21;

        /**
         * Identifier for QUERY_CONNECTED_USERS
         */
        public final static int QUERY_CONNECTED_USERS = 22;

        /**
         * Identifier for QUERY_USER_RESPONSE
         */
        public final static int QUERY_USER_RESPONSE = 23;

        /**
         * Identifier for BROADCAST_MESSAGE
         */
        public final static int BROADCAST_MESSAGE = 24;

        /**
         * Identifier for DIRECT_MESSAGE
         */
        public final static int DIRECT_MESSAGE = 25;

        /**
         * Identifier for FAILED_MESSAGE
         */
        public final static int FAILED_MESSAGE = 26;

        /**
         * Identifier for SEND_INSULT
         */
        public final static int SEND_INSULT = 27;
    }

    /**
     * Delimiter is message for parsing
     */
    public final static char DELIMITER = ' ';
}