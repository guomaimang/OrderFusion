package tech.hirsun.orderfusion.service.Impl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.hirsun.orderfusion.dao.PayDao;
import tech.hirsun.orderfusion.pojo.Pay;
import tech.hirsun.orderfusion.service.PayService;
import tech.hirsun.orderfusion.utils.SaltUtils;

import java.util.Date;

@Service
public class PayServiceImpl implements PayService {

    @Autowired
    private PayDao payDao;

    public Pay virtualPay(){
        Pay pay = new Pay(null,1, 2, "Virtual ID: " + SaltUtils.getRandomSalt(6), new Date());
        payDao.insert(pay);
        return pay;
    }

    @Override
    public Pay getPayInfo(Integer id) {
        return payDao.getPayById(id);
    }
}
