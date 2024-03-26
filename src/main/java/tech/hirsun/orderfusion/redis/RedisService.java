package tech.hirsun.orderfusion.redis;

import com.alibaba.fastjson2.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

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

    private <T> String beanToString(T value) {
        if (value == null) {
            return null;
        }
        Class<?> clazz = value.getClass();
        if (clazz == int.class || clazz == Integer.class || clazz == long.class || clazz == Long.class) {
            return String.valueOf(value);
        } else if (clazz == String.class) {
            return (String) value;
        } else {
            return JSON.toJSONString(value);
        }
    }

    private <T> T stringToBean(String value, Class<T> clazz) {
        if (value == null || value.length() == 0 || clazz == null) {
            return null;
        }
        if (clazz == int.class || clazz == Integer.class) {
            return (T) Integer.valueOf(value);
        } else if (clazz == long.class || clazz == Long.class) {
            return (T) Long.valueOf(value);
        } else if (clazz == String.class) {
            return (T) value;
        } else {
            return JSON.toJavaObject(JSON.parseObject(value), clazz);
        }
    }


}
