package Server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Represents the mock client helper method to test server side
 */
public class MockClient {

    private Socket socket;
    private OutputStream outputStream;

    /**
     * Constructor for MockClient
     *
     * @param port  thr port
     */
    public MockClient(int port, String username) {
        try {
            this.socket = new Socket("localhost", port);
            this.outputStream = socket.getOutputStream();
            start(username);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Start the mock client
     * @param username the username
     */
    public void start(String username) {
        String connectMessage = String.format("19 %d %s", username.length(), username);
        sendMessage(connectMessage);
    }

    /**
     * Stop the mock client
     */
    public void stop() {
        try {
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Send the message to the client side
     * @param message  the message
     */
    private void sendMessage(String message) {
        try {
            outputStream.write(message.getBytes());
            outputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}