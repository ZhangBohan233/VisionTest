package dvaTest.connection;

import java.io.IOException;
import java.net.InetAddress;

public class ClientManager {
    public static final int DEFAULT_PORT = 3456;

    private static Client currentClient;

    public static Client getCurrentClient() {
        return currentClient;
    }

    public static void startClient(String address, int port) throws IOException {
        if (currentClient != null) {
            // todo: close client
        }
        currentClient = new Client(address, port);
    }
}
