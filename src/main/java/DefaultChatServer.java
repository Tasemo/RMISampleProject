import java.net.InetSocketAddress;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class DefaultChatServer implements ChatServer {

    private final Map<String, InetSocketAddress> clients = new HashMap<>();

    public static void main(String[] args) throws RemoteException, AlreadyBoundException {
        ChatServer server = registerChatServer(1099);
        try (Scanner scanner = new Scanner(System.in)) {
            while (!Thread.interrupted()) {
                String input = scanner.next();
                if ("quit".equals(input) || "exit".equals(input)) {
                    Thread.currentThread().interrupt();
                } else if ("list".equals(input)) {
                    System.out.println(String.join(", ", server.list()));
                }
            }
        }
        UnicastRemoteObject.unexportObject(server, true);
    }

    @Override
    public String connect(String hostName, int port) {
        String id = UUID.randomUUID().toString();
        clients.put(id, InetSocketAddress.createUnresolved(hostName, port));
        return id;
    }

    @Override
    public void disconnect(String id) {
        clients.remove(id);
    }

    @Override
    public void send(String message, String sender) throws RemoteException, NotBoundException {
        for (Map.Entry<String, InetSocketAddress> client : clients.entrySet()) {
            if (!client.getKey().equals(sender)) {
                send(message, sender, client.getValue());
            }
        }
    }

    @Override
    public void send(String message, String sender, String recipient) throws RemoteException, NotBoundException {
        if (clients.containsKey(recipient)) {
            send(message, sender, clients.get(recipient));
        }
    }

    @Override
    public Set<String> list() {
        return new HashSet<>(clients.keySet());
    }

    private static ChatServer registerChatServer(int port) throws RemoteException, AlreadyBoundException {
        Registry registry = LocateRegistry.createRegistry(port);
        ChatServer chatServer = new DefaultChatServer();
        Remote stub = UnicastRemoteObject.exportObject(chatServer, port);
        registry.bind("ChatServer", stub);
        return chatServer;
    }

    private static void send(String message, String id, InetSocketAddress address) throws RemoteException, NotBoundException {
        if (message == null) {
            throw new IllegalArgumentException("The message must not be null!");
        }
        if (id == null) {
            throw new IllegalAccessError("The id of the sender must not be null!");
        }
        if (address == null) {
            throw new NotBoundException("Recipient with id " + id + " is not connected!");
        }
        Registry registry = LocateRegistry.getRegistry(address.getHostName(), address.getPort());
        ChatClient chatClient = (ChatClient) registry.lookup("ChatClient");
        chatClient.receive(message, id);
    }
}
