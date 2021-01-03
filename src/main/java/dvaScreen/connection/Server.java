package dvaScreen.connection;

import common.EventLogger;
import common.Signals;
import common.data.PrefSaver;
import dvaScreen.gui.ScreenMainView;
import dvaTest.connection.Client;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public abstract class Server extends Thread {

    protected final ScreenMainView mainView;

    Server(ScreenMainView mainView) {
        this.mainView = mainView;
    }

    public abstract void stopServer();

    public abstract void startListening() throws IOException;

    public abstract void sendMessage(byte signal) throws IOException;

    public abstract void sendMessage(byte[] data) throws IOException;

    public abstract boolean hasConnection();

    public abstract boolean isClientInputShutdown();

    public static class LanServer extends Server {

        private final int port;
        private ServerSocket serverSocket;
        private Socket clientSocket;
        private boolean closed = false;

        public LanServer(ScreenMainView mainView, int port) {
            super(mainView);
            this.port = port;
        }

        @Override
        public boolean hasConnection() {
            return clientSocket != null;
        }

        @Override
        public boolean isClientInputShutdown() {
            return clientSocket.isInputShutdown();
        }

        @Override
        public void run() {
            try {
                startListening();
            } catch (BindException e) {
                mainView.showCannotConnect(mainView.getBundle().getString("portOccupied"),
                        String.format(
                                mainView.getBundle().getString("closePortOccupant"),
                                PrefSaver.SCREEN_PREF));
            } catch (IllegalArgumentException e) {
                mainView.showCannotConnect(mainView.getBundle().getString("invalidPort"),
                        String.format(
                                mainView.getBundle().getString("changePortInPref"),
                                PrefSaver.SCREEN_PREF));
            } catch (IOException e) {
                EventLogger.log(e);
                mainView.showCannotConnect(mainView.getBundle().getString("cannotConnectToNet"),
                        "");
            } finally {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    EventLogger.log(e);
                }
            }
        }

        public void startListening() throws IOException {
            if (closed) return;
            System.out.println("Start waiting for connection");
            if (serverSocket == null || serverSocket.isClosed()) {
                // 解决一个serverSocket被自动关闭的bug
                serverSocket = new ServerSocket(port, 50, ServerManager.getThisAddress());
            }
            try {
                clientSocket = serverSocket.accept();
            } catch (SocketException e) {
                // No client has connected
                System.out.println("Socket server closed without connection!");
                return;
            }
            System.out.println("Connected! ");
            ServerSideListener listener = new ServerSideListener(
                    mainView,
                    this,
                    clientSocket.getInputStream(),
                    clientSocket.getOutputStream()
            );
            listener.setDaemon(true);
            listener.start();
        }

        public void stopServer() {
            if (serverSocket == null) {
                return;
            }
            closed = true;
            try {
                if (clientSocket != null && !clientSocket.isClosed()) {
                    sendMessage(Signals.DISCONNECT_BY_SERVER);
                }
            } catch (IOException e) {
                EventLogger.log(e);
            } finally {
                try {
                    serverSocket.close();
                    System.out.println("Server closed! ");
                } catch (IOException e) {
                    EventLogger.log(e);
                }
            }
        }

        public synchronized void sendMessage(byte signal) throws IOException {
            clientSocket.getOutputStream().write(signal);
            clientSocket.getOutputStream().flush();
        }

        public synchronized void sendMessage(byte[] data) throws IOException {
            clientSocket.getOutputStream().write(data);
            clientSocket.getOutputStream().flush();
        }
    }

    public static class LocalServer extends Server {

        private Client.LocalClient localClient;

        LocalServer(ScreenMainView mainView) {
            super(mainView);
        }

        public void setLocalClient(Client.LocalClient localClient) {
            this.localClient = localClient;
        }

        @Override
        public boolean isClientInputShutdown() {
            return false;
        }

        @Override
        public void stopServer() {
            try {
                localClient.shutdown();
            } catch (IOException e) {
                EventLogger.log(e);
            }
        }

        @Override
        public void startListening() throws IOException {

        }

        @Override
        public void sendMessage(byte signal) throws IOException {
            localClient.getOutputStream().write(signal);
        }

        @Override
        public void sendMessage(byte[] data) throws IOException {
            localClient.getOutputStream().write(data);
        }

        @Override
        public boolean hasConnection() {
            return localClient != null && !localClient.isDisconnected();
        }
    }
}
