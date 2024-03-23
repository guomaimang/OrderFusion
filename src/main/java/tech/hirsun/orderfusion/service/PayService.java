package tech.hirsun.orderfusion.service;

import tech.hirsun.orderfusion.pojo.Pay;

public interface PayService {

    public Pay virtualPay();

    public Pay getPayInfo(Integer id);
}
