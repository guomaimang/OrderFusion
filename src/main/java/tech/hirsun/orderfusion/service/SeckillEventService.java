package tech.hirsun.orderfusion.service;

import tech.hirsun.orderfusion.pojo.PageBean;
import tech.hirsun.orderfusion.pojo.SeckillEvent;

public interface SeckillEventService {

    public int create(SeckillEvent seckillEvent);

    public void update(SeckillEvent seckillEvent);

    public SeckillEvent getSeckillEventInfo(Integer id);

    public PageBean page(Integer pageNum, Integer pageSize, String keyword);

    public int minusStock(Integer seckillEventId, Integer goodsAmount);
}
