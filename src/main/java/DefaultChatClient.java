import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;
import java.util.regex.Pattern;

public class DefaultChatClient implements ChatClient {

    private static final Pattern RECIPIENT_SPLIT_PATTERN = Pattern.compile(": ");

    public static void main(String[] args) throws RemoteException, AlreadyBoundException, NotBoundException, UnknownHostException {
        ChatClient chatClient = registerChatClient(Integer.parseInt(args[1]));
        Registry registry = LocateRegistry.getRegistry(args[0], 1099);
        ChatServer chatServer = (ChatServer) registry.lookup("ChatServer");
        String id = chatServer.connect(InetAddress.getLocalHost().getHostName(), Integer.parseInt(args[1]));
        try (Scanner scanner = new Scanner(System.in)) {
            while (!Thread.interrupted()) {
                String input = scanner.nextLine();
                if ("quit".equals(input) || "exit".equals(input)) {
                    Thread.currentThread().interrupt();
                } else if ("list".equals(input)) {
                    System.out.println(String.join(", ", chatServer.list()));
                } else {
                    String[] recipientAndMessage = RECIPIENT_SPLIT_PATTERN.split(input);
                    if (recipientAndMessage.length == 1) {
                        chatServer.send(recipientAndMessage[0], id);
                    } else {
                        chatServer.send(recipientAndMessage[1], id, recipientAndMessage[0]);
                    }
                }
            }
        }
        chatServer.disconnect(id);
        UnicastRemoteObject.unexportObject(chatClient, true);
    }

    @Override
    public void receive(String message, String sender) {
        System.out.println(sender + ": " + message);
    }

    private static ChatClient registerChatClient(int port) throws RemoteException, AlreadyBoundException {
        Registry registry = LocateRegistry.createRegistry(port);
        ChatClient chatClient = new DefaultChatClient();
        Remote stub = UnicastRemoteObject.exportObject(chatClient, port);
        registry.bind("ChatClient", stub);
        return chatClient;
    }
}
