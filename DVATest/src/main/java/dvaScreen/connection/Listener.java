package dvaScreen.connection;

import dvaScreen.gui.ScreenMainView;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class Listener extends Thread {

    private ScreenMainView mainView;
    private Socket client;

    Listener(ScreenMainView mainView, Socket client) {
        this.client = client;
        this.mainView = mainView;
    }

    @Override
    public void run() {
        try {
            byte[] buf = new byte[1024];
            InputStream inputStream = client.getInputStream();
            int read;
            while (client.isConnected() && (read = inputStream.read(buf)) >= 0) {
                if (read == 1) {  // 单个信号
                    processSignal(buf[0]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processSignal(byte signal) {
        switch (signal) {
            case Signals.GREET:
                System.out.println("connected! ");
                mainView.closeConnectionWindow();
                break;
        }
    }
}
