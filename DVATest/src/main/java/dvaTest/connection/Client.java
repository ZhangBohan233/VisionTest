package dvaTest.connection;

import common.Signals;
import dvaTest.testCore.tests.TestUnit;

import java.io.IOException;
import java.net.*;

public class Client extends Thread {

    public Socket clientSocket;

    Client(String address, int port) throws IOException {
        clientSocket = new Socket();
        clientSocket.connect(new InetSocketAddress(address, port));

        sendMessage(Signals.GREET);
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

    public void disconnect() throws IOException {
        sendMessage(Signals.DISCONNECT);
        clientSocket.shutdownInput();
        clientSocket.shutdownOutput();
        clientSocket.close();
    }
}
