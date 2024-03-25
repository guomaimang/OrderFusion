package tech.hirsun.orderfusion.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import tech.hirsun.orderfusion.pojo.Goods;
import java.util.List;

@Mapper
public interface GoodsDao {


    // By Annotation
    @Select("select * from goods where id = #{id}")
    public Goods getGoodsById(@Param("id") int id);

    // By XML
    /**
     * @return the number of users
     */
    public Integer count(@Param("keyword") String keyword);

    /**
     *
     * @param start: the first row index
     * @param pageSize: the number of users in the page
     * @param keyword: the keyword to search
     *
     * @return the List of Goods
     */
    public List<Goods> list(@Param("start") int start,
                            @Param("pageSize") int pageSize,
                            @Param("keyword") String keyword);
    public int update(Goods goods);

    public int insert(Goods goods);

    @Update("update goods set stock = stock - #{amount} where id = #{id} and stock >= #{amount}")
    public int minusStock(@Param("id") int id, @Param("amount") int amount);
}
