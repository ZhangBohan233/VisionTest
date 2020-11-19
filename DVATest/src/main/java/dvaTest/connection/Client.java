package dvaTest.connection;

import common.Signals;
import dvaTest.gui.MainView;
import dvaTest.testCore.ITestController;
import dvaTest.testCore.TestController;
import dvaTest.testCore.tests.TestUnit;

import java.io.IOException;
import java.io.InputStream;
import java.net.*;

public class Client extends Thread {

    private final MainView mainView;
    private final Socket clientSocket;
    private boolean disconnected;
    private ITestController testController;
    private Listener listener;

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
    }

    public synchronized void sendMessage(byte signal) throws IOException {
        clientSocket.getOutputStream().write(signal);
    }

    public synchronized void sendTestUnit(TestUnit testUnit) throws IOException {
        sendMessage(testUnit.toByteArray());
    }

    synchronized void disconnectWithServerByClient() throws IOException {
        sendMessage(Signals.DISCONNECT_BY_CLIENT);
        disconnected = true;
        shutdown();
    }

    private synchronized void shutdown() throws IOException {
//        disconnected = true;
        clientSocket.shutdownInput();
        clientSocket.shutdownOutput();

        do {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (!clientSocket.isInputShutdown() || !clientSocket.isOutputShutdown());

//        clientSocket.getInputStream().close();
        clientSocket.close();
        System.out.println("client socket closed");
        listener.interrupt();
        System.out.println("Listener interrupted");
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
//                    disconnected = true;
                    shutdown();
                    ClientManager.discardCurrentClient();
                    mainView.setDisconnected();
                    break;
                case Signals.SCREEN_INTERRUPT:
                    if (testController != null) {
                        testController.interrupt();
                        testController.closeTestView();
                    }
                    break;
                default:
                    throw new IOException("Unknown signal " + b);
            }
        }
    }
}
