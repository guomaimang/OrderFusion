package tech.hirsun.orderfusion.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.hirsun.orderfusion.dao.UserDao;
import tech.hirsun.orderfusion.pojo.PageBean;
import tech.hirsun.orderfusion.pojo.User;

@Service
public interface UserService {

    User login(User user);

    User ssoLogin(String email, String displayName);

    User getUserInfo(Integer id);

    void update(User user);

    PageBean page(int start, int pageSize, String keyword);

    void add(User user);

    void lockSwitch(User user);
}