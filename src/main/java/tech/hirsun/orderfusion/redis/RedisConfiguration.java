package tech.hirsun.orderfusion.redis;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
@Getter @Setter
public class RedisConfiguration {

    @Value("${redis.host}")
    private String host;

    @Value("${redis.port}")
    private int port;

    @Value("${redis.password}")
    private String password;

    @Value("${redis.database}")
    private int database;

    @Value("${redis.timeout}")
    private int timeout;

    @Value("${redis.poolMaxIdle}")
    private int maxIdle;

    @Value("${redis.poolMaxTotal}")
    private int maxTotal;

    @Value("${redis.poolMaxWait}")
    private int maxWaitSeconds;

    public JedisPool JedisPoolFactory() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMaxTotal(maxTotal);
        jedisPoolConfig.setMaxWaitMillis(maxWaitSeconds*1000);
        JedisPool jedisPool = new JedisPool(jedisPoolConfig, host, port, timeout*1000, password, database);
        System.out.println("RedisConfiguration.JedisPoolFactory() called");
        return jedisPool;
    }
}
