package tech.hirsun.orderfusion.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tech.hirsun.orderfusion.pojo.User;

@Mapper
public interface UserDao {

    @Select("select * from user where id = #{id}")
    public User getUserById(@Param("id")int id);

    @Insert("insert into user(id, name) values (#{id}, #{name})")
    public int insert(User user);

}
