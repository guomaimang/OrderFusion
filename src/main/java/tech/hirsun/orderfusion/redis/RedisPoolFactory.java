package tech.hirsun.orderfusion.redis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Service
public class RedisPoolFactory {

    @Autowired
    RedisConfiguration redisConfiguration;

    @Bean
    public JedisPool jedisPoolFactory() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(redisConfiguration.getLettuce().getPool().getMaxIdle());
        jedisPoolConfig.setMaxTotal(redisConfiguration.getLettuce().getPool().getMaxActive());
        jedisPoolConfig.setMaxWait(java.time.Duration.ofSeconds(redisConfiguration.getLettuce().getPool().getMaxWait()));

        JedisPool jedisPool = new JedisPool(jedisPoolConfig, redisConfiguration.getHost(), redisConfiguration.getPort(),
                redisConfiguration.getTimeout() * 1000, redisConfiguration.getPassword(), redisConfiguration.getDatabase());
        return jedisPool;
    }

}
