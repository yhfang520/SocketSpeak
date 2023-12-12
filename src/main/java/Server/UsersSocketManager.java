package Server;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Represents the Users Socket Manager
 */
public class UsersSocketManager {

    private ConcurrentHashMap<String, Socket> connectedUsers;
    private int MAX_NUMBER_OF_USERS = 10;

    /**
     * Constructor for UsersSocketManager with the concurrent hashmap to sort the connected users
     *
     * @param connectedUsers    to store the connected users
     */
    public UsersSocketManager(ConcurrentHashMap connectedUsers) {
        this.connectedUsers = connectedUsers;
    }

    /**
     * Add new user to until the maximum number of users is reached
     * @param username the username
     * @param newUser new users
     * @return true if the user been added
     */
    public synchronized boolean add(String username, Socket newUser){
        if(connectedUsers.size()<this.MAX_NUMBER_OF_USERS){
            connectedUsers.put(username,newUser);
            return true;
        }
        return false;
    }

    /**
     * Removed the user
     * @param username the username
     * @return true if the user been removed
     */
    public synchronized boolean remove(String username){
        if(connectedUsers.remove(username)==null){
            return false;
        }
        return true;
    }

    /**
     * Queries the list of connected usernames
     * @return an array list containing all connected usernames
     */
    public synchronized ArrayList<String> queryAll(){
        ArrayList<String> userslist = new ArrayList<>();
        Iterator iterator = connectedUsers.keySet().iterator();
        while(iterator.hasNext()){
            userslist.add(new String((String) iterator.next()));
        }
        return userslist;
    }

    /**
     * Queries the total number of connected users
     * @return the number of connected users
     */
    public synchronized int queryNum(){
        return connectedUsers.size();
    }

    /**
     * Retrieves the concurrent hashmap
     * @return the connected users
     */
    public synchronized ConcurrentHashMap<String, Socket> getConnectedUsers() {
        return connectedUsers;
    }

    /**
     * Check if the user contains the username
     * @param username the username
     * @return true if user is connected
     */
    public synchronized boolean contains(String username) {
        return connectedUsers.containsKey(username);
    }

    /**
     * Get the socket
     * @param username the username
     * @return the socket associated with the username
     */
    public synchronized Socket getSocket(String username) {
            return connectedUsers.get(username);

    }

}
