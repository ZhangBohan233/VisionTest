package dvaTest.connection;

import java.io.IOException;

public class ClientManager {

    private static Client currentClient;

    public static Client getCurrentClient() {
        return currentClient;
    }

    public static Client startClient() throws IOException {
        return new Client();
    }
}
