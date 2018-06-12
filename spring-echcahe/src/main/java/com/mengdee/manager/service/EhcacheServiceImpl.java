package com.mengdee.manager.service;

import com.mengdee.manager.pojo.User;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * @Author: z.yu
 * @Date: 2018/06/12 10:47
 */
@Service
public class EhcacheServiceImpl implements EhcacheService {

    /**
     * 开启缓存，缓存方法的结果。缓存的对象是配置文件中的 HelloWorldCache 节点，以传入来的 param参数作为缓存数据的 key（默认）
     */
    @Cacheable(value = "HelloWorldCache", key = "#param")
    @Override
    public String getTimestamp(String param) {
        Long timestamp = System.currentTimeMillis();
        return timestamp.toString();
    }

    @Cacheable(value = "HelloWorldCache", key = "#key")
    @Override
    public String getDataFromDB(String key) {
        System.out.println("从数据库中获取数据...");
        return key + ":" + String.valueOf(Math.round(Math.random() * 1000000));
    }

    /**
     * 删除缓存数据
     */
    @CacheEvict(value = "HelloWorldCache", key = "#key")
    @Override
    public void removeDataAtDB(String key) {
        System.out.println("从数据库中删除数据");
    }

    /**
     * 缓存方法的结果，执行方法体。相当于刷新缓存了。
     */
    @CachePut(value = "HelloWorldCache", key = "#key")
    @Override
    public String refreshData(String key) {
        System.out.println("模拟从数据库中加载数据");
        return key + "::" + String.valueOf(Math.round(Math.random() * 1000000));
    }

    // ------------------------------------------------------------------------
    // 将缓存保存到名称为UserCache中，键为"user:"字符串加上userId值，如 'user:1'
    @Override
    @Cacheable(value = "UserCache", key = "'user:' + #userId")
    public User findById(String userId) {
        System.out.println("模拟从数据库中查询数据");
        return (User) new User("1", "mengdee");
    }

    /**
     * 将缓存保存进UserCache中，并当参数userId的长度小于12时才保存进缓存，默认使用参数值及类型作为缓存的key
     * 保存缓存需要指定key，value， value的数据类型，不指定key默认和参数名一样如："1"
     */
    @Override
    @Cacheable(value = "UserCache", condition = "#userId.length()<12")
    public boolean isReserved(String userId) {
        System.out.println("UserCache:" + userId);
        return false;
    }

    //清除掉UserCache中某个指定key的缓存
    @Override
    @CacheEvict(value = "UserCache", key = "'user:' + #userId")
    public void removeUser(String userId) {
        System.out.println("UserCache remove:" + userId);
    }

    //清除掉UserCache中全部的缓存
    @Override
    @CacheEvict(value = "UserCache", allEntries = true)
    public void removeAllUser() {
        System.out.println("UserCache delete all");
    }

    //清除掉UserCache中全部的缓存
    @CacheEvict(value = "UserCache", allEntries = true)
    public final void setReservedUsers(String[] reservedUsers) {
        System.out.println("UserCache deleteall");
    }
}
