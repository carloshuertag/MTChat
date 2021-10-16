package Models;

import java.net.SocketAddress;

/**
 *
 * @author huert
 */
public class User {
    private static int usersCount = 1;
    private final int UID;
    private String name;
    private SocketAddress sAddr;

    public User() {
        UID = usersCount;
        usersCount++;
    }

    public User(String name, SocketAddress sAddr) {
        this.name = name;
        this.sAddr = sAddr;
        UID = usersCount;
        usersCount++;
    }

    public int getUID() {
        return UID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SocketAddress getsAddr() {
        return sAddr;
    }

    public void setsAddr(SocketAddress sAddr) {
        this.sAddr = sAddr;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("User{UID=").append(UID);
        sb.append(", name=").append(name);
        sb.append(", sAddr=").append(sAddr);
        sb.append('}');
        return sb.toString();
    }

}
