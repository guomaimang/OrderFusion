package tech.hirsun.orderfusion.dao;

import org.apache.ibatis.annotations.*;
import tech.hirsun.orderfusion.pojo.User;

import java.util.List;

@Mapper
public interface UserDao {

    // By Annotation
    @Select("select * from user where id = #{id}")
    public User getUserById(@Param("id")int id);

    @Select("select * from user where email = #{email}")
    public User getUserByEmail(@Param("email")String email);

    // By XML
    /**
     *
     * @param start: the first row index
     * @param pageSize: the number of users in the page
     * @param keyword: the keyword to search
     *
     * @return the List of users
     */
    public List<User> list(@Param("start") int start,
                           @Param("pageSize") int pageSize,
                           @Param("keyword") String keyword);

    public int update(User user);

    public int insert(User user);

    /**
     * @return the number of users
     */
    public int count(String keyword);

}