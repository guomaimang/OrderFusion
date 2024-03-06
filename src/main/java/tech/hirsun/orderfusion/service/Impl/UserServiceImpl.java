package tech.hirsun.orderfusion.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import tech.hirsun.orderfusion.dao.UserDao;
import tech.hirsun.orderfusion.pojo.User;
import tech.hirsun.orderfusion.service.UserService;

public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;
    public User login(User user) {
        User dbUser = userDao.getUserByEmail(user.getEmail());

        if(dbUser.getPassword() == user.getPassword()) {
            return dbUser;
        }
    }

    public void update(User user) {
    }

    public void register(User user) {
    }
}
