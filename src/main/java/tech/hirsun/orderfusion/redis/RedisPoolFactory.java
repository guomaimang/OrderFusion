package tech.hirsun.orderfusion.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import tech.hirsun.orderfusion.redis.RedisConfiguration;

@Service
public class RedisPoolFactory {

    @Autowired
    RedisConfiguration redisConfiguration;

    @Bean
    public JedisPool JedisPoolFactory() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxIdle(redisConfiguration.getPoolMaxIdle());
        poolConfig.setMaxTotal(redisConfiguration.getPoolMaxTotal());
        poolConfig.setMaxWaitMillis(redisConfiguration.getPoolMaxWait() * 1000);
        JedisPool jp = new JedisPool(poolConfig, redisConfiguration.getHost(), redisConfiguration.getPort(),
                redisConfiguration.getTimeout()*1000, redisConfiguration.getPassword(), 0);
        return jp;
    }

}