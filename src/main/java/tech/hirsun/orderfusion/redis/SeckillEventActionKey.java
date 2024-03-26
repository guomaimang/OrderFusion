package tech.hirsun.orderfusion.redis;

public class SeckillEventActionKey extends BasePrefix{

        public SeckillEventActionKey(String prefix) {
            super(86400, prefix);
        }

        public static SeckillEventActionKey byParams = new SeckillEventActionKey("params");
}
