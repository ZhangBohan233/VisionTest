package dvaScreen.connection;

import dvaScreen.gui.ScreenMainView;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread {

    private int port;
    private ScreenMainView mainView;
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
            clientSocket = serverSocket.accept();

            Listener listener = new Listener(mainView, clientSocket);
            listener.start();
        } catch (IOException e) {
            e.printStackTrace();
            throw new ServerException();
        }
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    public void stopServer() throws IOException {
        if (serverSocket == null) {
            throw new IOException("Server not started.");
        }
        serverSocket.close();
    }
}
