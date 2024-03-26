package tech.hirsun.orderfusion.service.Impl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.hirsun.orderfusion.dao.SeckillEventDao;
import tech.hirsun.orderfusion.pojo.PageBean;
import tech.hirsun.orderfusion.pojo.SeckillEvent;
import tech.hirsun.orderfusion.redis.PageBeanKey;
import tech.hirsun.orderfusion.redis.RedisService;
import tech.hirsun.orderfusion.service.SeckillEventService;

import java.util.List;

@Service
public class SeckillEventServiceImpl implements SeckillEventService{

    @Autowired
    private SeckillEventDao seckillEventDao;

    @Autowired
    private RedisService redisService;

    @Override
    public void create(SeckillEvent seckillEvent) {
        SeckillEvent draftSeckillEvent = SeckillEvent.getDraftObjForDB(seckillEvent);
        seckillEventDao.insert(draftSeckillEvent);

        // update redis
        String redisKey = "PageBean<seckillEvents> " +
                "pageNum: " + 1 +
                "pageSize: " + 10 +
                "keyword: " + draftSeckillEvent.getTitle();
        redisService.delete(PageBeanKey.byParams,redisKey);
        redisKey = "PageBean<seckillEvents> " +
                "pageNum: " + 1 +
                "pageSize: " + 10 +
                "keyword: " + null;
        redisService.delete(PageBeanKey.byParams,redisKey);
    }

    @Override
    public void update(SeckillEvent seckillEvent) {
        SeckillEvent draftSeckillEvent = SeckillEvent.getDraftObjForDB(seckillEvent);
        seckillEventDao.update(draftSeckillEvent);
    }

    @Override
    public SeckillEvent getSeckillEventInfo(Integer id) {
        return seckillEventDao.getSeckillEventById(id);
    }

    @Override
    public PageBean page(Integer pageNum, Integer pageSize, String keyword) {
        // from redis
        String redisKey = "PageBean<seckillEvents> " +
                "pageNum: " + pageNum +
                "pageSize: " + pageSize +
                "keyword: " + keyword;
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

}
