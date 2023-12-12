package Client;

import java.io.IOException;
import java.net.Socket;

/**
 * Represents the Chat Client
 */
public class ChatClient {

    /**
     * Main method
     * @param args the grammar JSON directory path
     */
    public static void main(String[] args){
        String serverIP = "localhost";
        if(args.length==3){serverIP=args[2];}
        int port = Integer.parseInt(args[0]);
        String username = args[1];


        try{
            Socket socket = new Socket(serverIP,port);
            System.out.println(username+" is connect to "+serverIP+" "+port);

            ClientCommunication clientCommunication = new ClientCommunication(socket,username);
            Thread newClient  = new Thread(clientCommunication);
            newClient.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
