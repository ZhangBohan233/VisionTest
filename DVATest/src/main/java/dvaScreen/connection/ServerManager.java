package dvaScreen.connection;

import common.data.AutoSavers;
import dvaScreen.gui.ScreenMainView;

import java.io.IOException;
import java.net.InetAddress;

public class ServerManager {
    public static final int DEFAULT_PORT = 3456;

    private static InetAddress thisAddress;
    private static int port;

    private static Server currentServer;

    public static boolean startServer(ScreenMainView mainView) {
        port = AutoSavers.getPrefSaver().getInt("port", DEFAULT_PORT);
        try {
            generateIpAddress();
            if (currentServer == null) {
                currentServer = new Server(mainView, port);
                currentServer.start();
            } else {
                return false;
            }
            return true;
        } catch (IOException | ServerException e) {
            return false;
        }
    }

    public static void stopServer() throws IOException {
        if (currentServer == null) {
            throw new IOException("No server running");
        } else {
            currentServer.stopServer();
        }
    }

    public static boolean hasConnection() {
        return currentServer != null && currentServer.getClientSocket() != null;
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
