package tech.hirsun.orderfusion.dao;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SeckillOrdersetActionDao {

    @Insert("insert into seckill_orderset_action(user_id, seckill_event_id) values(#{userId}, #{seckillEventId})")
    public void insert(Integer userId, Integer seckillEventId);

    @Select("select count(*) from seckill_orderset_action where user_id = #{userId} and seckill_event_id = #{seckillEventId}")
    public int count(Integer userId, Integer seckillEventId);

}
