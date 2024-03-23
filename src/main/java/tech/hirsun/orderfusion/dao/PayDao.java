package tech.hirsun.orderfusion.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import tech.hirsun.orderfusion.pojo.Pay;

@Mapper
public interface PayDao {

    // By Annotation
    @Select("select * from pay where id = #{id}")
    public Pay getPayById(Integer id);

    // By XML
    public Integer insert(Pay pay);

    public void update(Pay pay);

}
