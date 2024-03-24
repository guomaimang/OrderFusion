package tech.hirsun.orderfusion.service.Impl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.hirsun.orderfusion.dao.SeckillEventDao;
import tech.hirsun.orderfusion.pojo.PageBean;
import tech.hirsun.orderfusion.pojo.SeckillEvent;
import tech.hirsun.orderfusion.service.SeckillEventService;

import java.util.List;

@Service
public class SeckillEventServiceImpl implements SeckillEventService{

    @Autowired
    private SeckillEventDao seckillEventDao;

    @Override
    public void create(SeckillEvent seckillEvent) {
        SeckillEvent draftSeckillEvent = SeckillEvent.getDraftObjForDB(seckillEvent);
        seckillEventDao.insert(draftSeckillEvent);
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
        int count = seckillEventDao.count(keyword);

        int start = (pageNum-1) * pageSize;
        List<SeckillEvent> seckillEvents = seckillEventDao.list(start, pageSize, keyword);
        return new PageBean(count, seckillEvents,Math.floorDiv(count, pageSize) + 1, pageNum);
    }


}
