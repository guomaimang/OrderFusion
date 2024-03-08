package tech.hirsun.orderfusion.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tech.hirsun.orderfusion.pojo.User;
import java.util.Date;


@SpringBootTest
class UserDaoTest {

    @Autowired
    private UserDao userDao;

    @Test
    void getUserById() {
    }

    @Test
    void getUserByEmail() {
    }

    @Test
    void insert() {
        User user = new User("hanffsee", "hanji@2df8dh", "123");
        userDao.insert(user);
        System.out.println(user.getId());
    }

    @Test
    void update() {
        User user = new User();
        user.setId(6);
        user.setName("jsjs");
        System.out.println(userDao.update(user));
    }
}