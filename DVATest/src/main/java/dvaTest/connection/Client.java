package dvaTest.connection;

import java.io.IOException;
import java.io.OutputStream;
import java.net.*;

public class Client extends Thread {

    public Socket clientSocket;

    Client(String address, int port) throws IOException {
        clientSocket = new Socket();
        clientSocket.connect(new InetSocketAddress(address, port));

        OutputStream outputStream = clientSocket.getOutputStream();
        outputStream.write(Signals.GREET);
    }
}
