package tech.hirsun.orderfusion.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.hirsun.orderfusion.dao.UserDao;
import tech.hirsun.orderfusion.pojo.User;
import tech.hirsun.orderfusion.service.UserService;
import tech.hirsun.orderfusion.utils.HashUtil;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;
    public User login(User user) {
        User dbUser = userDao.getUserByEmail(user.getEmail());
        if (dbUser == null) {
            return null;
        }

        String saltedPassword = HashUtil.formPlainPassToDBPass(user.getPassword(), dbUser.getRandomSalt());

        if(dbUser.getPassword().equals(saltedPassword)) {
            return new User(dbUser.getName(), dbUser.getEmail(), dbUser.getAvatarUri(), dbUser.getIsAdmin());
        }else {
            return null;
        }
    }

    public void update(User user) {
    }

    public void register(User user) {
    }
}
