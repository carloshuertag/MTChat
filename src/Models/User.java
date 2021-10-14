package Models;

/**
 *
 * @author huert
 */
public class User {
    private static int usersCount = 1;
    private final int UID;
    private String name;

    public User() {
        UID = usersCount;
        usersCount++;
    }

    public User(String name) {
        this.name = name;
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("User{UID=").append(UID);
        sb.append(", name=").append(name);
        sb.append('}');
        return sb.toString();
    }
}
