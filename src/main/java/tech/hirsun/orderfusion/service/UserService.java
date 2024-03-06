package tech.hirsun.orderfusion.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.hirsun.orderfusion.dao.UserDao;
import tech.hirsun.orderfusion.pojo.User;

@Service
public interface UserService {

    User login(User user);

    void update(User user);

    void register(User user);

}
