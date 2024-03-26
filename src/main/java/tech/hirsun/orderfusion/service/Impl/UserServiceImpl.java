package tech.hirsun.orderfusion.service.Impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tech.hirsun.orderfusion.dao.UserDao;
import tech.hirsun.orderfusion.pojo.PageBean;
import tech.hirsun.orderfusion.pojo.User;
import tech.hirsun.orderfusion.redis.RedisService;
import tech.hirsun.orderfusion.redis.UserKey;
import tech.hirsun.orderfusion.service.UserService;
import tech.hirsun.orderfusion.utils.HashUtil;
import tech.hirsun.orderfusion.utils.SaltUtils;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    RedisService redisService;

    @Value("${orderfusion.fixedSalt}")
    private String fixedSalt;

    public User login(User user) {
        User dbUser = userDao.getUserByEmail(user.getEmail());

        if (dbUser == null) {
            return null;
        }
        String saltedPassword = HashUtil.formPlainPassToDBPass(user.getPassword(), dbUser.getRandomSalt(),fixedSalt);

        if(dbUser.getPassword().equals(saltedPassword)) {
            return new User(dbUser.getId(), dbUser.getName(), dbUser.getEmail(), dbUser.getAvatarUri(), dbUser.getIsAdmin());
        }else {
            return null;
        }
    }

    @Override
    public User ssoLogin(String email, String displayName) {
        User dbUser = userDao.getUserByEmail(email);
        if (dbUser != null) {
            return dbUser;
        }

        User draftUser = new User();

        draftUser.setName(displayName);
        draftUser.setEmail(email);

        String randomSalt = SaltUtils.getRandomSalt(6);
        draftUser.setRandomSalt(randomSalt);
        draftUser.setPassword(HashUtil.formPlainPassToDBPass(SaltUtils.getRandomSalt(6), randomSalt, fixedSalt));

        draftUser.setRegisterTime(new Date());

        userDao.insert(draftUser);

        return draftUser;
    }

    @Override
    public User getUserInfo(Integer id) {
        // from redis
        User user = redisService.get(UserKey.byId,String.valueOf(id),User.class);
        if (user != null) {
            return user;
        }

        // from db
        user = userDao.getUserById(id);
        if (user != null) {
            redisService.set(UserKey.byId,String.valueOf(id),user);
        }
        return userDao.getUserById(id);
    }

    public void update(User user) {
        User draftUser = new User();
        draftUser.setId(user.getId());

        if (user.getName() != null){
            if (user.getName().length() > 0){
                draftUser.setName(user.getName());
            }
        }

        if (user.getPassword() != null){
            if (user.getPassword().length() > 0){
                String randomSalt = SaltUtils.getRandomSalt(6);

                draftUser.setRandomSalt(randomSalt);
                draftUser.setPassword(HashUtil.formPlainPassToDBPass(user.getPassword(), randomSalt, fixedSalt));
            }
        }

        if (user.getEmail() != null){
            if (user.getEmail().length() > 0){
                draftUser.setEmail(user.getEmail());
            }
        }
        userDao.update(draftUser);
        // update redis
        redisService.delete(UserKey.byId,String.valueOf(user.getId()));
    }

    @Override
    public PageBean page(int pageNum, int pageSize, String keyword) {
        int count = userDao.count(keyword);

        int start = (pageNum-1) * pageSize;
        List<User> users = userDao.list(start, pageSize, keyword);

        return new PageBean(count, users,Math.floorDiv(count, pageSize) + 1, pageNum);
    }

    @Override
    public void add(User user) {
        User draftUser = new User();

        draftUser.setName(user.getName());
        draftUser.setEmail(user.getEmail());

        String randomSalt = SaltUtils.getRandomSalt(6);
        draftUser.setRandomSalt(randomSalt);
        draftUser.setPassword(HashUtil.formPlainPassToDBPass(user.getPassword(), randomSalt, fixedSalt));

        draftUser.setRegisterTime(new Date());

        userDao.insert(draftUser);
    }

    @Override
    public void lockSwitch(User user) {
        User draftUser = new User();
        draftUser.setId(user.getId());
        if (user.getIsFrozen() == 0) {
            draftUser.setIsFrozen(1);
        } else {
            draftUser.setIsFrozen(0);
        }
        // update database
        userDao.update(draftUser);
        // update redis
        redisService.delete(UserKey.byId,String.valueOf(user.getId()));
    }
}
