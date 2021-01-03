package dvaTest.connection;

import common.Signals;
import dvaScreen.connection.Server;
import dvaTest.gui.MainView;
import dvaTest.testCore.ITestController;
import dvaTest.testCore.tests.TestUnit;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

public abstract class Client extends Thread {

    final MainView mainView;
    ITestController testController;
    protected InputStream clientInputStream;
    protected OutputStream clientOutputStream;

    Client(MainView mainView) {
        this.mainView = mainView;
    }

    public InputStream getInputStream() {
        return clientInputStream;
    }

    public OutputStream getOutputStream() {
        return clientOutputStream;
    }

    public void setTestController(ITestController testController) {
        this.testController = testController;
    }

    public synchronized void sendMessage(byte[] array) throws IOException {
        getOutputStream().write(array);
        getOutputStream().flush();
    }

    public synchronized void sendMessage(byte signal) throws IOException {
        getOutputStream().write(signal);
        getOutputStream().flush();
    }

    public synchronized void sendTestUnit(TestUnit testUnit) throws IOException {
        sendMessage(testUnit.toByteArray());
    }

    abstract boolean isDisconnected();

    public abstract void shutdown() throws IOException;

    abstract void disconnectWithServerByClient() throws IOException;

    abstract boolean isInputShutdown();

    public static class LanClient extends Client {
        final Socket clientSocket;
        private final ClientSideListener listener;
        private boolean disconnected;

        LanClient(String address, int port, MainView mainView) throws IOException {
            super(mainView);

            clientSocket = new Socket();
            clientSocket.connect(new InetSocketAddress(address, port));

            clientInputStream = clientSocket.getInputStream();
            clientOutputStream = clientSocket.getOutputStream();

            sendMessage(Signals.GREET);

            listener = new ClientSideListener(this);
            listener.setDaemon(true);
            listener.start();
        }

        @Override
        public boolean isInputShutdown() {
            return clientSocket.isInputShutdown();
        }

        synchronized void disconnectWithServerByClient() throws IOException {
            try {
                sendMessage(Signals.DISCONNECT_BY_CLIENT);
                shutdown();
            } catch (SocketException e) {
                // 屏幕已经被关闭
            }
            disconnected = true;
            listener.interrupt();
            System.out.println("Listener interrupted");
        }

        public synchronized void shutdown() throws IOException {
            shutdownEssential();
        }

        @Override
        public boolean isDisconnected() {
            return disconnected;
        }

        private synchronized void shutdownEssential() throws IOException {
            clientSocket.shutdownInput();
            clientSocket.shutdownOutput();

            do {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (!clientSocket.isInputShutdown() || !clientSocket.isOutputShutdown());

            clientSocket.close();
            System.out.println("client socket closed");
        }
    }

    public static class LocalClient extends Client {

        LocalClient(MainView mainView) {
            super(mainView);
        }

        @Override
        public boolean isDisconnected() {
            return false;
        }

        @Override
        public void shutdown() throws IOException {
            clientInputStream.close();
            clientOutputStream.flush();
            clientOutputStream.close();
        }

        @Override
        void disconnectWithServerByClient() throws IOException {

        }

        @Override
        boolean isInputShutdown() {
            return false;
        }
    }
}
