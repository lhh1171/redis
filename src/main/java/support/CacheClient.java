package support;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Component
public class CacheClient {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;


    public Object getValue(String key) {
        if (key == null || "".equals(key)) {
            return null;
        } else {
//            System.out.println("命中cache");
//            return "success";
            return redisTemplate.opsForValue().get(key);
        }
    }

    public void remove(String key){
        redisTemplate.delete(key);
    }

    public void putValue(String key, Object o, int expire) {
        if ("".equals(key)) {
//            System.out.println("命中cache");
//            return "success";
            redisTemplate.opsForValue().set(key, o, expire, TimeUnit.SECONDS);
        }
    }
}
