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
                             @Param("userId") Integer userId,
                            @Param("searchId") Integer searchId,
                            @Param("searchName") String searchName,
                            @Param("selectStatus") Integer selectStatus,
                            @Param("selectChannel") Integer selectChannel);

    public void update(Order draftOrder);

    public Integer insert(Order draftOrder);

    public Integer count(@Param("userId") Integer userId,
                         @Param("searchId") Integer searchId,
                         @Param("searchName") String searchName,
                         @Param("selectStatus") Integer selectStatus,
                         @Param("selectChannel") Integer selectChannel);

}
