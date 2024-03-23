package tech.hirsun.orderfusion.service;

import tech.hirsun.orderfusion.pojo.Pay;

public interface PayService {

    public void create(Pay pay);

    public void finishPay(Integer id, Integer method, String transactionId);

    public Pay getPayInfo(Integer id);
}
