package tech.hirsun.orderfusion.service.Impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.hirsun.orderfusion.dao.GoodsDao;
import tech.hirsun.orderfusion.pojo.Goods;
import tech.hirsun.orderfusion.pojo.PageBean;
import tech.hirsun.orderfusion.service.GoodsService;

import java.util.List;

@Slf4j
@Service
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private GoodsDao goodsDao;

    @Override
    public Goods getGoodsInfo(Integer id) {
        return goodsDao.getGoodsById(id);
    }

    @Override
    public void update(Goods goods) {
        Goods draftGoods = Goods.getDraftObjForDB(goods);
        goodsDao.update(draftGoods);
    }

    @Override
    public PageBean page(Integer pageNum, Integer pageSize, String keyword) {
        int count = goodsDao.count(keyword);

        int start = (pageNum-1) * pageSize;
        List<Goods> goods = goodsDao.list(start, pageSize, keyword);

        return new PageBean(count, goods,Math.floorDiv(count, pageSize) + 1, pageNum);
    }

    @Override
    public void add(Goods goods) {
        Goods draftGoods = Goods.getDraftObjForDB(goods);
        goodsDao.insert(draftGoods);
    }

}