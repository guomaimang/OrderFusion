package tech.hirsun.orderfusion.service.Impl;
import org.springframework.stereotype.Service;
import tech.hirsun.orderfusion.pojo.Pay;
import tech.hirsun.orderfusion.service.PayService;

@Service
public class PayServiceImpl implements PayService {
    @Override
    public void create(Pay pay) {

    }

    @Override
    public void finishPay(Integer id, Integer method, String transactionId) {

    }

    @Override
    public void getPayInfo(Integer id) {

    }
}
