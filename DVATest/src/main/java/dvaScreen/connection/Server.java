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

    public Server(ScreenMainView mainView, int port) {
        this.port = port;
        this.mainView = mainView;
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port, 50, ServerManager.getThisAddress());
            startListening();
        } catch (BindException e) {
            EventLogger.log(e);
            mainView.showCannotConnect(mainView.getBundle().getString("portOccupied"),
                    mainView.getBundle().getString("changePortInPref1") +
                            PrefSaver.SCREEN_PREF +
                            mainView.getBundle().getString("changePortInPref2"));
        } catch (IOException e) {
            EventLogger.log(e);
            mainView.showCannotConnect(mainView.getBundle().getString("cannotConnectToNet"),
                    "");
        }
    }

    public void startListening() throws IOException {
        try {
            clientSocket = serverSocket.accept();
        } catch (SocketException e) {
            // No client has connected
            return;
        }

        ServerSideListener listener = new ServerSideListener(mainView, this, clientSocket);
        listener.start();
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    public synchronized void stopServer() throws IOException {
        if (serverSocket == null) {
            return;
//            throw new IOException("Server not started.");
        }
        if (clientSocket != null && !clientSocket.isClosed()) {
            sendMessage(Signals.DISCONNECT_BY_SERVER);
        }
        serverSocket.close();
    }

    public synchronized void sendMessage(byte signal) throws IOException {
        clientSocket.getOutputStream().write(signal);
    }
}
