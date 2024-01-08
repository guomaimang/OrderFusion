package tech.hirsun.orderfusion.redis;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spring.data.redis")
@Data
public class RedisConfiguration {

    private String host;
    private int port;
    private String password;
    private int database;
    private int timeout;
    private Lettuce lettuce;

    public static class Lettuce {
        @Getter @Setter
        private Pool pool;

        @Data
        public static class Pool {
            private int maxActive;
            private int maxIdle;
            private int maxWait;
        }
    }
}
