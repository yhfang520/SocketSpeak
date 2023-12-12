package Client;

import java.util.regex.Pattern;

/**
 * Represents User Commands
 */
public class UserCommands {

    /**
     * Display the commands menu
     */
    public final static String HELP = "?";

    /**
     * Log off from the chat server
     */
    public final static String LOGOFF = "logoff";

    /**
     * Query connected users
     */
    public final static String WHO = "who";

    /**
     * Symbol fot direct message
     */
    public final static String DIRECT_SYMBOL = "@";

    /**
     * Regular expression for direct message
     */
    public final static String DIRECT = "@[a-zA-Z0-9_]+";

    /**
     * Command for broadcast message to all users
     */
    public final static String BROADCAST = "@all";

    /**
     * Send the insult message
     */
    public final static String INSULT_SYMBOL = "!";

    /**
     * Regular expression for insult message
     */
    public final static String INSULT = "![a-zA-Z0-9_]+";

    /**
     * The combined command
     */
    public final static String combinedCommand = String.join("|",LOGOFF,WHO,DIRECT,BROADCAST,INSULT);

    /**
     * Combined the regular expression pattern for all
     */
    public final static Pattern COMMAND_PATTERNS = Pattern.compile(combinedCommand);
}