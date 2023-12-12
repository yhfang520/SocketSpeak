package Server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Represents the Chat Server
 */
public class ChatServer {
private final static String QUIT = "quit";

  /**
   * Main method
   * @param args the grammar JSON directory path
   */
  public static void main(String[] args) {
    int port = Integer.parseInt(args[0]);
    startServer(port);
  }

  /**
   * Start the server
   * @param port the port user pass in
   * @return true if it connects correctly
   */
  public static boolean startServer(int port){
    try {
      System.out.println("IP: "+ InetAddress.getLocalHost().getHostAddress());
    } catch (UnknownHostException e) {
      throw new RuntimeException(e);
    }
    System.out.println("start listening on "+ port);
    String input = "";

    UsersSocketManager usersSocketManager = new UsersSocketManager(new ConcurrentHashMap<>());
    try (ServerSocket serverSocket = new ServerSocket(port)) {

      while (true & !input.equals(QUIT)) {
        Socket clientSocket = serverSocket.accept();
        Thread newClient = new Thread(new ClientConnectionHandler(clientSocket,
            usersSocketManager));
        newClient.start();
      }
      return true;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }finally {
      return false;
    }
  }
}