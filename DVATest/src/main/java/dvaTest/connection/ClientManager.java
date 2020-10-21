package dvaTest.connection;

import dvaTest.gui.MainView;

import java.io.IOException;

public class ClientManager {
    public static final int DEFAULT_PORT = 3456;

    private static Client currentClient;

    public static Client getCurrentClient() {
        return currentClient;
    }

    public static void startClient(String address, int port, MainView mainView) throws IOException {
        if (currentClient != null) {
            // todo: close client
        }
        currentClient = new Client(address, port, mainView);
    }

    public static void closeCurrentClient() throws IOException {
        if (currentClient != null) {
            currentClient.disconnectFromServer();
            currentClient = null;
        }
    }

    public static void discardCurrentClient() {
        currentClient = null;
    }
}
