package dvaTest.connection;

import common.Signals;
import dvaTest.gui.MainView;
import dvaTest.testCore.ITestController;
import dvaTest.testCore.tests.TestUnit;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

public class Client extends Thread {

    private final MainView mainView;
    private final Socket clientSocket;
    private final Listener listener;
    private boolean disconnected;
    private ITestController testController;

    Client(String address, int port, MainView mainView) throws IOException {
        this.mainView = mainView;

        clientSocket = new Socket();
        clientSocket.connect(new InetSocketAddress(address, port));

        sendMessage(Signals.GREET);

        listener = new Listener();
        listener.setDaemon(true);
        listener.start();
    }

    public synchronized void sendMessage(byte[] array) throws IOException {
        clientSocket.getOutputStream().write(array);
        clientSocket.getOutputStream().flush();
    }

    public synchronized void sendMessage(byte signal) throws IOException {
        clientSocket.getOutputStream().write(signal);
        clientSocket.getOutputStream().flush();
    }

    public synchronized void sendTestUnit(TestUnit testUnit) throws IOException {
        sendMessage(testUnit.toByteArray());
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

    private synchronized void shutdown() throws IOException {
        shutdownEssential();
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

    public void setTestController(ITestController testController) {
        this.testController = testController;
    }

    class Listener extends Thread {

        Listener() {
        }

        @Override
        public void run() {
            try {
                byte[] buf = new byte[1024];
                InputStream inputStream = clientSocket.getInputStream();
                int read;
                while (!(disconnected || clientSocket.isInputShutdown()) &&
                        (read = inputStream.read(buf)) > 0) {
                    if (read == 1) {
                        processSignal(buf[0]);
                    } else {
                        throw new IOException("Unknown signal size. ");
                    }
                }
                // inputStream 将会在Client.shutdown() 内关闭
            } catch (SocketException e) {
                // 从客户端断开了连接
                System.out.println("socket exception: disconnected from client");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private synchronized void processSignal(byte b) throws IOException {
            switch (b) {
                case Signals.DISCONNECT_BY_SERVER:
                    shutdown();
                    mainView.setDisconnected();
                    break;
                case Signals.SCREEN_INTERRUPT:
                    if (testController != null) {
                        testController.interruptByScreen();
                        testController.closeTestView();
                    }
                    break;
                default:
                    throw new IOException("Unknown signal " + b);
            }
        }
    }
}
