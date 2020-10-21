package dvaTest.connection;

import common.Signals;
import dvaTest.gui.MainView;
import dvaTest.testCore.tests.TestUnit;

import java.io.IOException;
import java.io.InputStream;
import java.net.*;

public class Client extends Thread {

    private final MainView mainView;
    private final Socket clientSocket;

    Client(String address, int port, MainView mainView) throws IOException {
        this.mainView = mainView;

        clientSocket = new Socket();
        clientSocket.connect(new InetSocketAddress(address, port));

        sendMessage(Signals.GREET);

        Listener listener = new Listener();
        listener.start();
    }

    public synchronized void sendMessage(byte[] array) throws IOException {
        clientSocket.getOutputStream().write(array);
    }

    public synchronized void sendMessage(byte signal) throws IOException {
        clientSocket.getOutputStream().write(signal);
    }

    public synchronized void sendTestUnit(TestUnit testUnit) throws IOException {
        sendMessage(testUnit.toByteArray());
    }

    void disconnectFromServer() throws IOException {
        sendMessage(Signals.DISCONNECT_FROM_CLIENT);
        shutdown();
    }

    private void shutdown() throws IOException {
        clientSocket.shutdownInput();
        clientSocket.shutdownOutput();
        clientSocket.close();
    }

    class Listener extends Thread {

        private boolean disconnected;

        Listener() {
        }

        @Override
        public void run() {
            try {
                byte[] buf = new byte[1024];
                InputStream inputStream = clientSocket.getInputStream();
                int read;
                while (!(disconnected || clientSocket.isInputShutdown()) && (read = inputStream.read(buf)) >= 0) {
                    if (read == 1) {
                        processSignal(buf[0]);
                    } else {
                        throw new IOException("Unknown signal size. ");
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void processSignal(byte b) throws IOException {
            switch (b) {
                case Signals.DISCONNECT_FROM_SERVER:
                    disconnected = true;
                    shutdown();
                    ClientManager.discardCurrentClient();
                    mainView.setDisconnected();
                    break;
            }
        }
    }
}
