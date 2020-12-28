package dvaTest.connection;

import common.EventLogger;
import common.Signals;
import dvaTest.gui.MainView;
import javafx.beans.property.BooleanProperty;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
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

    private static String getFirstTwoIp() throws IOException {
        String thisAddr = InetAddress.getLocalHost().getHostAddress();
        String firstThree = thisAddr.substring(0, thisAddr.lastIndexOf('.'));
        return thisAddr.substring(0, firstThree.lastIndexOf('.'));
    }

    public static void listLanDevices(ObservableList<DeviceItem> connectibleList, int port, BooleanProperty running) {
        try {
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

            String firstTwoIp = getFirstTwoIp();  // 同一个局域网下ip前两位必定相同

            while (running.get() && matcher.find()) {
                String ip = matcher.group();
                if (ip.startsWith(firstTwoIp)) {
                    InetAddress address = InetAddress.getByName(ip);
                    DeviceItem di = checkAddress(address, port);
                    if (di != null)
                        connectibleList.add(di);
                }
            }

        } catch (Exception e) {
            EventLogger.log(e);
        }
    }

    private synchronized static DeviceItem checkAddress(InetAddress address, int port) {
        try {
            System.out.print("Reaching " + address + "... ");
            if (address.isReachable(50)) {
                System.out.print("Checking... ");
                Socket socket = new Socket();
                try {
                    socket.connect(new InetSocketAddress(address.getHostAddress(), port));
                    socket.setSoTimeout(1000);
                    socket.getOutputStream().write(Signals.TEST_CONNECTION);
                    byte[] buffer = new byte[1024];
                    int read = socket.getInputStream().read(buffer);
                    System.out.println("Success");
                    return new DeviceItem(address, new String(buffer, 0, read, StandardCharsets.UTF_8));
                } catch (IOException e2) {
                    System.out.println("Failed");
                    //
                } finally {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        EventLogger.log(e);
                    }
                }
            } else {
                System.out.println();
            }
        } catch (IOException e) {
            EventLogger.log(e);
        }
        return null;
    }
}
