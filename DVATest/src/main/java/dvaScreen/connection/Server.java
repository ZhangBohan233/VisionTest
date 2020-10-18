package dvaScreen.connection;

import dvaScreen.gui.ScreenMainView;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

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
        } catch (SocketException e) {
            // No client has connected
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
        if (clientSocket != null) {
            clientSocket.shutdownInput();
            clientSocket.shutdownOutput();
            clientSocket.close();
        }
        serverSocket.close();
    }

    public void sendMessage(byte signal) throws IOException {
        clientSocket.getOutputStream().write(signal);
    }
}