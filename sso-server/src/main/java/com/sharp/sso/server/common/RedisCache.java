package com.sharp.sso.server.common;

import org.apache.ibatis.cache.CacheException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class RedisCache<T> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private RedisTemplate<String, T> redisTemplate;

    public void setRedisTemplate(RedisTemplate<String, T> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private static final String KEY_SET_PREFIX = "_set";
    private static final String KEY_LIST_PREFIX = "_list";

    public T get(String key) {
        logger.debug("get key [{}]", key);
        try {
            if (key == null) {
                return null;
            } else {
                return redisTemplate.opsForValue().get(key);
            }
        } catch (Exception e) {
            logger.error("get key [{}] exception", key, e);
            throw new CacheException(e);
        }
    }

    public T set(String key, T value) {
        logger.debug("setExpire key [{}]", key);
        try {
            redisTemplate.opsForValue().set(key, value);
            return value;
        } catch (Exception e) {
            logger.error("setExpire key [{}] exception", key, e);
            throw new CacheException(e);
        }
    }

    public T set(String key, T value, long timeout) {
        logger.debug("setExpire key [{}]", key);
        try {
            redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
            return value;
        } catch (Exception e) {
            logger.error("setExpire key [{}] exception", key, e);
            throw new CacheException(e);
        }
    }

    public void delete(String key) {
        logger.debug("delete key [{}]", key);
        try {
            redisTemplate.delete(key);
        } catch (Exception e) {
            logger.error("delete key [{}] exception", key, e);
            throw new CacheException(e);
        }
    }

    /**********************Set集合操作**********************/

    @SuppressWarnings("unchecked")
    public void setSet(String k, T value, long timeout) {
        String key = KEY_SET_PREFIX + k;
        logger.debug("setSet key [{}]", key);
        try {
            SetOperations<String, T> valueOps = redisTemplate.opsForSet();
            valueOps.add(key, value);
            if (timeout > 0) {
                redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
            }
        } catch (Exception e) {
            logger.error("setSet key [{}] exception", key, e);
            throw new CacheException(e);
        }
    }

    public void setSet(String k, T v) {
        setSet(k, v, -1);
    }

    @SuppressWarnings("unchecked")
    public void setSet(String k, Set<T> v, long timeout) {
        String key = KEY_SET_PREFIX + k;
        logger.debug("set set key [{}]", key);
        try {
            SetOperations<String, T> valueOps = redisTemplate.opsForSet();
            valueOps.add(key, (T) v.toArray());
            if (timeout > 0) {
                redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
            }
        } catch (Exception e) {
            logger.error("setSet key [{}] exception", key, e);
            throw new CacheException(e);
        }
    }

    public void setSet(String k, Set<T> v) {
        setSet(k, v, -1);
    }

    public Set<T> getSet(String k) {
        String key = KEY_SET_PREFIX + k;
        logger.debug("getSet key [{}]", key);
        try {
            SetOperations<String, T> setOps = redisTemplate.opsForSet();
            return setOps.members(key);
        } catch (Exception e) {
            logger.error("getSet key [{}] exception", key, e);
            throw new CacheException(e);
        }
    }


    /*******************List操作**********************/
    public void setList(String k, T v, long timeout) {
        String key = KEY_LIST_PREFIX + k;
        logger.debug("setList key [{}]", key);
        try {
            ListOperations<String, T> listOps = redisTemplate.opsForList();
            listOps.rightPush(key, v);
            if (timeout > 0) {
                redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
            }
        } catch (Exception e) {
            logger.error("setList key [{}] exception", key, e);
            throw new CacheException(e);
        }
    }

    public void setList(String k, List<T> v, long timeout) {
        String key = KEY_LIST_PREFIX + k;
        logger.debug("setList key [{}]", key);
        try {
            ListOperations<String, T> listOps = redisTemplate.opsForList();
            listOps.rightPushAll(key, v);
            if (timeout > 0)
                redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
        } catch (Exception e) {
            logger.error("setList key [{}] exception!", key, e);
            throw new CacheException(e);
        }
    }

    public void setList(String k, List<T> v) {
        setList(k, v, -1);
    }

    public List<T> getList(String k, long start, long end) {
        String key = KEY_LIST_PREFIX + k;
        logger.debug("setList key [{}]", key);
        try {
            ListOperations<String, T> listOps = redisTemplate.opsForList();
            return listOps.range(key, start, end);
        } catch (Exception e) {
            logger.error("getList key [{}] exception!", key, e);
            throw new CacheException(e);
        }
    }

    public long getListSize(String k) {
        String key = KEY_LIST_PREFIX + k;
        logger.debug("getListSize key [{}]", key);
        try {
            return redisTemplate.opsForList().size(k);
        } catch (Exception e) {
            logger.error("getListSize key [{}] exception", key);
            throw new CacheException(e);
        }
    }

    public void setMap(String key, String mapKey, T mapValue) {
        redisTemplate.opsForHash().putIfAbsent(key, mapKey, mapValue);
    }

    public void deleteMap(String key, String mapKey) {
        redisTemplate.opsForHash().delete(key, mapKey);
    }

    public T getMap(String key, String mapkey) {
        HashOperations<String, String, T> hashOperations = redisTemplate.opsForHash();
        return hashOperations.get(key, mapkey);
    }

    public List<T> getMapValues(String key) {
        HashOperations<String, String, T> hashOperations = redisTemplate.opsForHash();
        return hashOperations.values(key);
    }
}
