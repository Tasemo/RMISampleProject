import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ChatClient extends Remote {

    /**
     * Called when a message was received for this client.
     *
     * @param message the message received, can never be null
     * @param sender the original sender of the message, can never be null
     */
    void receive(String message, String sender) throws RemoteException;
}
