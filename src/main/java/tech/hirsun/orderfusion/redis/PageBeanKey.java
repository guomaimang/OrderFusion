package tech.hirsun.orderfusion.redis;

public class PageBeanKey extends BasePrefix{
    private PageBeanKey(String prefix) {
        super(90,prefix);
    }
    public static PageBeanKey byParams = new PageBeanKey("params");
}
