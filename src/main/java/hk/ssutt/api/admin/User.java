package hk.ssutt.api.admin;

/**
 * Created by fau on 3/6/14.
 */
public class User {
    private static String name;
    private static String password;

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public static String getName() {
        return name;
    }
    public static String getPassword() {
        return password;
    }
}
