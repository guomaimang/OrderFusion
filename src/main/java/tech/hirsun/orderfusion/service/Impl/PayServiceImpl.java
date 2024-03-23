package tech.hirsun.orderfusion.service.Impl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.hirsun.orderfusion.dao.OrderDao;
import tech.hirsun.orderfusion.dao.PayDao;
import tech.hirsun.orderfusion.pojo.Order;
import tech.hirsun.orderfusion.pojo.Pay;
import tech.hirsun.orderfusion.service.PayService;

import java.util.Date;

@Service
public class PayServiceImpl implements PayService {

    @Autowired
    private PayDao payDao;

    public Pay virtualPay(){
        Pay pay = new Pay(null,1, 2, "Virtual Transaction ID", new Date());
        payDao.insert(pay);
        return pay;
    }

    @Override
    public Pay getPayInfo(Integer id) {
        return payDao.getPayById(id);
    }
}
