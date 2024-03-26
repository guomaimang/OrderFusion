package tech.hirsun.orderfusion.redis;

public class SeckillEventKey extends BasePrefix{

    private SeckillEventKey(String prefix) {
        super(prefix);
    }

    public static SeckillEventKey byId = new SeckillEventKey("id");

}
