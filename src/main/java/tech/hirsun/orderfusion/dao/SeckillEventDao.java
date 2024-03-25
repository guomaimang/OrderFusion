package tech.hirsun.orderfusion.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import tech.hirsun.orderfusion.pojo.SeckillEvent;

import java.util.List;

@Mapper
public interface SeckillEventDao {

    // By Annotation
    @Select("select * from seckill_event where id = #{id}")
    public SeckillEvent getSeckillEventById(Integer id);

    // By XML
    public int insert(SeckillEvent draftSeckillEvent);

    public int update(SeckillEvent draftSeckillEvent);

    public int count(String keyword);

    public List<SeckillEvent> list(int start, Integer pageSize, String keyword);

    @Update("update seckill_event set seckill_stock = seckill_stock - #{goodsAmount} where id = #{seckillEventId} and seckill_stock >= #{goodsAmount}")
    int minusStock(Integer seckillEventId, Integer goodsAmount);
}
