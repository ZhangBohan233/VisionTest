package dvaTest.connection;

import java.net.InetAddress;

@SuppressWarnings("unused")
public class DeviceItem {

    private final InetAddress address;
    private final String name;

    DeviceItem(InetAddress address, String name) {
        this.address = address;
        this.name = name;
    }

    public InetAddress getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }

    public String getHostAddress() {
        return address.getHostAddress();
    }
}
