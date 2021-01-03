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

    public static boolean startServer(ScreenMainView mainView, boolean isLocal) {
        if (isLocal) {
            return startLocalServer(mainView);
        } else {
            return startLanServer(mainView);
        }
    }

    private static boolean startLanServer(ScreenMainView mainView) {
        port = AutoSavers.getPrefSaver().getInt("port", DEFAULT_PORT);
        try {
            generateIpAddress();
            if (currentServer == null) {
                currentServer = new Server.LanServer(mainView, port);
                currentServer.start();
            } else {
                return false;
            }
            return true;
        } catch (IOException | ServerException e) {
            return false;
        }
    }

    private static boolean startLocalServer(ScreenMainView mainView) {
        if (currentServer == null) {
            currentServer = new Server.LocalServer(mainView);
            currentServer.start();  // 43.2
            return true;
        } else {
            return false;
        }
    }

    public static void stopServer() {
        if (currentServer != null) {
            currentServer.stopServer();
        }
    }

    public static boolean hasConnection() {
        return currentServer != null && currentServer.hasConnection();
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
