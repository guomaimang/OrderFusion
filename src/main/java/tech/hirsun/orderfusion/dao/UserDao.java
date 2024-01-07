package tech.hirsun.orderfusion.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import tech.hirsun.orderfusion.model.User;

@Mapper
public interface UserDao {

    @Select("select * from user where id = #{id}")
    public User getUserById(int id);





}
