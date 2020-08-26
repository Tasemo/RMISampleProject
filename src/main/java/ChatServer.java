import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Set;

public interface ChatServer extends Remote {

    /**
     * Connects a client with the given hostname and port and returns an unique id
     * used for further identification.
     *
     * @param hostName the hostname to connect to
     * @param port the port to connect to
     * @throws IllegalArgumentException if hostname and port are not valid
     * @return the id of the client, can never be null
     */
    String connect(String hostName, int port) throws RemoteException, IllegalArgumentException;

    /**
     * Disconnects a client identified by its id. Does nothing if the client
     * is not connected or does not exit.
     *
     * @param id the id of the client
     */
    void disconnect(String id) throws RemoteException;

    /**
     * Sends the message to all connected clients with the exception of the sender.
     *
     * @param message the message to send
     * @param sender the id of the sender
     * @throws NotBoundException if a client can not be reached
     * @throws IllegalArgumentException if message or sender are null
     */
    void send(String message, String sender) throws RemoteException, NotBoundException, IllegalArgumentException;

    /**
     * Sends the message to the client specified by its id. It is allowed to specify the own id.
     * Does nothing if no recipient with that id exists.
     *
     * @param message the message to send, can be null
     * @param sender the id of the sender
     * @param recipient the id of the recipient
     * @throws NotBoundException if the specified recipient can not be reached
     * @throws IllegalArgumentException if message or sender are null
     */
    void send(String message, String sender, String recipient) throws RemoteException, NotBoundException, IllegalArgumentException;

    /**
     * Returns a set of ids of all currently connected clients. Returns an empty set if
     * no client is connected.
     *
     * @return the set of clients, can never be null
     */
    Set<String> list() throws RemoteException;
}
