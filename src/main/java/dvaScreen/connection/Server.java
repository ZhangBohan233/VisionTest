package dvaScreen.connection;

import common.EventLogger;
import common.Signals;
import common.data.PrefSaver;
import dvaScreen.gui.ScreenMainView;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class Server extends Thread {

    private final int port;
    private final ScreenMainView mainView;
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private boolean closed = false;

    public Server(ScreenMainView mainView, int port) {
        this.port = port;
        this.mainView = mainView;
    }

    @Override
    public void run() {
        try {
//            serverSocket = new ServerSocket(port, 50, ServerManager.getThisAddress());
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
//            e.printStackTrace();
            return;
        }
        System.out.println("Connected! ");
        ServerSideListener listener = new ServerSideListener(mainView, this, clientSocket);
        listener.start();
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    public void stopServer() {
        if (serverSocket == null) {
            return;
        }
        closed = true;
//        System.out.println("Closing server");
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
    }
}
