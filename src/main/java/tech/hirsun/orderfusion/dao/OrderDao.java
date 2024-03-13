package tech.hirsun.orderfusion.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tech.hirsun.orderfusion.pojo.Order;

import java.util.List;

@Mapper
public interface OrderDao {

    //By Annotation

    @Select("select * from `order` where id = #{id}")
    public Order getOrderById(Integer id);

    // By XML
    public List<Order> list(@Param("start") int start,
                     @Param("pageSize") int pageSize,
                     @Param("keyword") String keyword,
                     @Param("userid") Integer userid);

    public void update(Order draftOrder);

    public int insert(Order draftOrder);

    public Integer count(@Param("keyword") String keyword,
                     @Param("userid") Integer userid);

}
