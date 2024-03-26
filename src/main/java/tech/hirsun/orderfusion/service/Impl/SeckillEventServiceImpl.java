package tech.hirsun.orderfusion.service.Impl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.hirsun.orderfusion.dao.SeckillEventDao;
import tech.hirsun.orderfusion.pojo.PageBean;
import tech.hirsun.orderfusion.pojo.SeckillEvent;
import tech.hirsun.orderfusion.redis.PageBeanKey;
import tech.hirsun.orderfusion.redis.RedisService;
import tech.hirsun.orderfusion.redis.SeckillEventKey;
import tech.hirsun.orderfusion.service.SeckillEventService;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class SeckillEventServiceImpl implements SeckillEventService{

    @Autowired
    private SeckillEventDao seckillEventDao;

    @Autowired
    private RedisService redisService;

    @Override
    public int create(SeckillEvent seckillEvent) {
        SeckillEvent draftSeckillEvent = SeckillEvent.getDraftObjForDB(seckillEvent);
        if (draftSeckillEvent.getStartTime().before(new Date()) ||
                draftSeckillEvent.getEndTime().before(new Date()) ||
                draftSeckillEvent.getStartTime().after(draftSeckillEvent.getEndTime())){
            return -1;
        }
        seckillEventDao.insert(draftSeckillEvent);

        // update redis
        redisService.set(SeckillEventKey.byId, draftSeckillEvent.getId().toString(), draftSeckillEvent);
        String redisKey = "PageBean<seckillEvents>" +
                " pageNum: " + 1 +
                " pageSize: " + 10 +
                " keyword: " + draftSeckillEvent.getTitle();
        redisService.delete(PageBeanKey.byParams,redisKey);
        redisKey = "PageBean<seckillEvents>" +
                " pageNum: " + 1 +
                " pageSize: " + 10 +
                " keyword: " + null;
        redisService.delete(PageBeanKey.byParams,redisKey);

        return 1;
    }

    @Override
    public void update(SeckillEvent seckillEvent) {
        SeckillEvent dbSeckillEvent = seckillEventDao.getSeckillEventById(seckillEvent.getId());
        SeckillEvent draftSeckillEvent = SeckillEvent.getDraftObjForDB(seckillEvent);
        if (dbSeckillEvent.getStartTime().before(new java.util.Date())) {
            draftSeckillEvent.setGoodsId(null);
            draftSeckillEvent.setTitle(null);
            draftSeckillEvent.setSeckillPrice(null);
            draftSeckillEvent.setStartTime(null);
            draftSeckillEvent.setSeckillStock(null);
            draftSeckillEvent.setPurchaseLimitNum(null);
        }

        // update db
        seckillEventDao.update(draftSeckillEvent);

        // update redis
        redisService.delete(SeckillEventKey.byId, seckillEvent.getId().toString());

        String redisKey = "PageBean<seckillEvents>" +
                " pageNum: " + 1 +
                " pageSize: " + 10 +
                " keyword: " + draftSeckillEvent.getTitle();
        redisService.delete(PageBeanKey.byParams,redisKey);
        redisKey = "PageBean<seckillEvents>" +
                " pageNum: " + 1 +
                " pageSize: " + 10 +
                " keyword: " + null;
        redisService.delete(PageBeanKey.byParams,redisKey);
    }

    @Override
    public SeckillEvent getSeckillEventInfo(Integer id) {
        // from redis
        SeckillEvent seckillEvent = redisService.get(SeckillEventKey.byId, id.toString(), SeckillEvent.class);
        if (seckillEvent != null) {
            return seckillEvent;
        }

        // from db
        seckillEvent = seckillEventDao.getSeckillEventById(id);
        if (seckillEvent != null) {
            redisService.set(SeckillEventKey.byId, id.toString(), seckillEvent);
        }
        return seckillEventDao.getSeckillEventById(id);
    }

    @Override
    public PageBean page(Integer pageNum, Integer pageSize, String keyword) {
        // from redis
        String redisKey = "PageBean<seckillEvents>" +
                " pageNum: " + pageNum +
                " pageSize: " + pageSize +
                " keyword: " + keyword;
        PageBean pageBean = redisService.get(PageBeanKey.byParams,redisKey,PageBean.class);
        if (pageBean != null) {
            return pageBean;
        }

        // from db
        int count = seckillEventDao.count(keyword);
        int start = (pageNum-1) * pageSize;
        List<SeckillEvent> seckillEvents = seckillEventDao.list(start, pageSize, keyword);
        pageBean = new PageBean(count, seckillEvents,Math.floorDiv(count, pageSize) + 1, pageNum);
        redisService.set(PageBeanKey.byParams,redisKey,pageBean);
        return pageBean;
    }

    @Override
    public int minusStock(Integer seckillEventId, Integer goodsAmount) {
        return seckillEventDao.minusStock(seckillEventId, goodsAmount);
    }

}
