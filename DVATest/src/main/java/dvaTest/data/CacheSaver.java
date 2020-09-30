package dvaTest.data;

import dvaTest.connection.ClientManager;

public class CacheSaver {

    public static String getLastUsedPort() {
        return String.valueOf(ClientManager.DEFAULT_PORT);
    }
}
