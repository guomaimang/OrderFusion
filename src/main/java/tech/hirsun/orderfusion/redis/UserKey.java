package tech.hirsun.orderfusion.redis;

public class UserKey extends BasePrefix {
    private UserKey(String prefix) {
        super(prefix);
    }
    public static UserKey byId = new UserKey("id");

}