package tech.hirsun.orderfusion.redis;

import lombok.Getter;

public abstract class BasePrefix implements KeyPrefix{

    @Getter
    private final int expireSeconds;

    private final String prefix;

    public BasePrefix(int expireSeconds,String prefix){
        this.expireSeconds = expireSeconds;
        this.prefix = prefix;
    }

    public BasePrefix(String prefix){
        this(0,prefix); // 0 means never expire
    }

    public String getPrefix(){
        String className = getClass().getSimpleName();
        return className + ":" + prefix;
    }





}
