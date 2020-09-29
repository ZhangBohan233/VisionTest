package dvaTest.connection;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client extends Thread {

    public static int DEFAULT_PORT = 3456;
    private int port = DEFAULT_PORT;

    public Socket clientSocket;

    public Client() {

    }
}
