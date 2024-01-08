package tech.hirsun.orderfusion.redis;

public interface KeyPrefix {

    public int getExpireSeconds();
    public String getPrefix();

}
