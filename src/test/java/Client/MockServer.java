package Client;

import java.io.IOException;
import java.net.Socket;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;

/**
 * Represents the mock server helper method to test client side
 */
public class MockServer {
    private ServerSocket serverSocket;

    /**
     * Constructor for MockServer
     *
     * @param port  thr port
     */
    public MockServer(int port) {
        try {
            this.serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Start the mock server, it will accept connection until interrupted
     */
    public void start() {
        new Thread(() -> {
            try {
                while(!Thread.interrupted()) {
                    Socket clientSocket = serverSocket.accept();
                    handleClient(clientSocket);
                }
            } catch(IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    /**
     * Handle the client connection
     * @param clientSocket  the client socket
     */
    private void handleClient(Socket clientSocket) {
        new Thread(() -> {
            try(InputStream inputStream = clientSocket.getInputStream();
            OutputStream outputStream = clientSocket.getOutputStream()) {
            } catch(IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    /**
     * Stop the mock server
     */
    public void stop() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}