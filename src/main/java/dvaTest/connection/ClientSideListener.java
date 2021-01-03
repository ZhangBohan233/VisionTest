package dvaTest.connection;

import common.Signals;

import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;

class ClientSideListener extends Thread {

    private final Client client;

    ClientSideListener(Client client) {
        this.client = client;
    }

    @Override
    public void run() {
        try {
            byte[] buf = new byte[1024];
            InputStream inputStream = client.getInputStream();
            int read;
            while (!(client.isDisconnected() || client.isInputShutdown()) &&
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
                client.shutdown();
                client.mainView.setDisconnected();
                break;
            case Signals.SCREEN_INTERRUPT:
                if (client.testController != null) {
                    client.testController.interruptByScreen();
                    client.testController.closeTestView();
                }
                break;
            default:
                throw new IOException("Unknown signal " + b);
        }
    }
}
