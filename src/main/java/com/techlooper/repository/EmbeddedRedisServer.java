package com.techlooper.repository;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import redis.clients.jedis.Protocol;
import redis.embedded.RedisServer;

/**
 * Created by phuonghqh on 1/2/15.
 */
public class EmbeddedRedisServer implements InitializingBean, DisposableBean {

    private RedisServer redisServer;

    public void afterPropertiesSet() throws Exception {
        redisServer = new RedisServer(Protocol.DEFAULT_PORT);
        redisServer.start();
    }

    public void destroy() throws Exception {
        if (redisServer != null) {
            redisServer.stop();
        }
    }
}