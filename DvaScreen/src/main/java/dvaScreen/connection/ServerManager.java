package dvaScreen.connection;

import java.io.IOException;
import java.net.InetAddress;

public class ServerManager {
    public static final int DEFAULT_PORT = 3456;

    private static InetAddress thisAddress;
    private static int port = DEFAULT_PORT;

    private static Server currentServer;

    public static boolean initialize() {
        try {
            generateIpAddress();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static Server getCurrentServer() {
        return currentServer;
    }

    public static void generateIpAddress() throws IOException {
        thisAddress = InetAddress.getLocalHost();
    }

    public static InetAddress getThisAddress() {
        return thisAddress;
    }

    public static int getPort() {
        return port;
    }
}
