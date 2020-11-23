package dvaTest.connection;

import common.EventLogger;
import dvaTest.gui.MainView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientManager {
    public static final String DEFAULT_IP = "192.168.186.1";

    private static Client currentClient;

    public static Client getCurrentClient() {
        return currentClient;
    }

    public static void startClient(String address, int port, MainView mainView) throws IOException {
        if (currentClient != null) {
            // todo: close client
        }
        currentClient = new Client(address, port, mainView);
    }

    public static void closeAndDiscardCurrentClient() throws IOException {
        if (currentClient != null) {
            currentClient.disconnectWithServerByClient();
            currentClient = null;
        } else {
            System.out.println("No client to close.");
        }
    }

    public static void listLanDevices() {
        try {
//            InetAddress localAddr = InetAddress.getLocalHost();

//            byte[] currentAddr = new byte[4];
//            System.arraycopy(localAddr.getAddress(), 0, currentAddr, 0, 3);  // 前三部分代表网络

//            for (int i = 1; i <= 255; i++) {
//                currentAddr[3] = (byte) i;
//                InetAddress addr = InetAddress.getByAddress(currentAddr);
//                new AddressChecker(addr, reachable).start();
//            }

            BufferedReader br = new BufferedReader(
                    new InputStreamReader(Runtime.getRuntime().exec("arp -a").getInputStream()));

            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                builder.append(line).append('\n');
            }

            br.close();

            Pattern pat = Pattern.compile("(\\d+.\\d+.\\d+.\\d+)");
            Matcher matcher = pat.matcher(builder.toString());

            List<InetAddress> reachable = new ArrayList<>();
            while (matcher.find()) {
                String ip = matcher.group();
                new AddressChecker(InetAddress.getByName(ip), reachable).start();
            }

        } catch (Exception e) {
            EventLogger.log(e);
        }
    }

    private static class AddressChecker extends Thread {

        private final InetAddress address;
        private final List<InetAddress> reachableAddresses;

        AddressChecker(InetAddress address, List<InetAddress> reachableAddresses) {
            this.address = address;
            this.reachableAddresses = reachableAddresses;
        }

        @Override
        public void run() {

            try {
                if (address.isReachable(1000)) {
//                    reachableAddresses.add(address);
//                    System.out.println(address.getCanonicalHostName() + " " + address.getHostAddress());

                    Socket socket = new Socket();
                    try {
                        socket.connect(new InetSocketAddress(address.getHostAddress(), 3456));
                        System.out.println(address);
                    } catch (IOException e2) {
                        System.out.println("Failed " + address);
                    } finally {
                        try {
                            socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (IOException e) {
                EventLogger.log(e);
            }
        }
    }
}
