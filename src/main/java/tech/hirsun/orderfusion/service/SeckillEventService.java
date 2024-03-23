package tech.hirsun.orderfusion.service;

import tech.hirsun.orderfusion.pojo.SeckillEvent;

public interface SeckillEventService {

    public Integer create(SeckillEvent seckillEvent);

    public SeckillEvent update(SeckillEvent seckillEvent);

    public SeckillEvent getSeckillEventInfo(Integer id);

}
