package tech.hirsun.orderfusion.redis;

import com.alibaba.fastjson2.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import static tech.hirsun.orderfusion.utils.StringAndBeanConventer.beanToString;
import static tech.hirsun.orderfusion.utils.StringAndBeanConventer.stringToBean;

@Service
public class RedisService {

    @Autowired
    JedisPool jedisPool;

    public <T> T get(KeyPrefix prefix, String key, Class<T> clazz) {
        try (Jedis jedis = jedisPool.getResource()) {
            String value = jedis.get(prefix.getPrefix() + key);
            return stringToBean(value, clazz);
        }
    }

    public <T> void set(KeyPrefix prefix, String key, T value) {
        try (Jedis jedis = jedisPool.getResource()) {
            String rvalue = beanToString(value);
            if (prefix.getExpireSeconds() <= 0){
                jedis.set(prefix.getPrefix() + key, rvalue);
            }else{
                jedis.setex(prefix.getPrefix() + key, prefix.getExpireSeconds(), rvalue);
            }
            jedis.set(prefix.getPrefix() + key, rvalue);
        }
    }

    public boolean exists(KeyPrefix prefix, String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.exists(prefix.getPrefix() + key);
        }
    }

    public boolean delete(KeyPrefix prefix, String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.del(prefix.getPrefix() + key) > 0;
        }
    }


    public Long incr(KeyPrefix prefix, String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.incr(prefix.getPrefix() + key);
        }
    }

    public Long decr(KeyPrefix prefix, String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.decr(prefix.getPrefix() + key);
        }
    }

}
