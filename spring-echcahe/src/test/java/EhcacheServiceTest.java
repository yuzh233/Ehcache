import com.mengdee.manager.pojo.User;
import com.mengdee.manager.service.EhcacheService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author: z.yu
 * @Date: 2018/06/12 11:04
 */
public class EhcacheServiceTest extends SpringTestCase {

    @Autowired
    private EhcacheService ehcacheService;

    /**
     * 测试缓存有效时间
     * 有效时间是5秒，第一次和第二次获取的值是一样的，因第三次是5秒之后所以会获取新的值
     * getTimestamp方法被 @Cacheable(value = "HelloWorldCache", key = "#param")修饰
     * 有效时间设定：timeToLiveSeconds="5"
     */
    @Test
    public void testTimestamp() throws InterruptedException {
        System.out.println("第一次调用：" + ehcacheService.getTimestamp("param"));
        Thread.sleep(2000);
        System.out.println("2秒之后调用：" + ehcacheService.getTimestamp("param"));
        Thread.sleep(4000);
        System.out.println("再过4秒之后调用：" + ehcacheService.getTimestamp("param"));
    }

    /**
     * 测试 @Cacheable 注解和 @CacheEvict 注解修饰的方法
     */
    @Test
    public void testCache() {
        String key = "zhangsan";
        String value = ehcacheService.getDataFromDB(key); // 第一次从数据库中获取数据，执行方法体
        System.out.println(value);

        value = ehcacheService.getDataFromDB(key);  // 再获取，从缓存中拿，不执行方法体
        System.out.println(value);

        ehcacheService.removeDataAtDB(key); // 从数据库中删除数据
        value = ehcacheService.getDataFromDB(key);  // 删除之后获取（缓存数据删除了，所以要重新获取，执行方法体）
        System.out.println(value);
    }

    /**
     * 测试 @Cacheable 注解和 @CachePut 注解修饰的方法
     */
    @Test
    public void testPut() {
        String key = "mengdee";
        String data = ehcacheService.refreshData(key);  // 模拟从数据库中加载数据
        System.out.println(data);

        data = ehcacheService.getDataFromDB(key); //模拟从数据库中取数据，已经有缓存数据，不执行方法体
        System.out.println(data);

        ehcacheService.refreshData(key);  // 模拟从数据库中加载数据，这一次是刷新数据，不仅会缓存结果，还会执行方法体
        data = ehcacheService.getDataFromDB(key);
        System.out.println(data);
    }


    @Test
    public void testFindById() {
        User user = ehcacheService.findById("1"); // 模拟从数据库中查询数据
        System.out.println(user);

        user = ehcacheService.findById("1");//第二次就不执行方法了
        System.out.println(user);
    }

    /**
     * 测试 @Cacheable 注解的 condition 参数
     * condition = "#userId.length()<12" 参数长度小于12才放入缓存（方法还是会执行的）
     */
    @Test
    public void testIsReserved() {
        ehcacheService.isReserved("123"); //第一次执行
        ehcacheService.isReserved("123"); //第二次缓存取

        ehcacheService.isReserved("1231231321456478979879"); //不符合condition条件，会执行方法，不会放入缓存
        ehcacheService.isReserved("1231231321456478979879");
    }


    /**
     * 添加指定缓存 —— 删除指定缓存 —— 重新获取
     */
    @Test
    public void testRemoveUser() {
        // 线添加到缓存
        ehcacheService.findById("1");

        // 再删除
        ehcacheService.removeUser("1");

        // 如果不存在会执行方法体
        ehcacheService.findById("1");
    }

    /**
     * 删除所有缓存，不管有没有key。
     */
    @Test
    public void testRemoveAllUser() {
        ehcacheService.findById("1");
        ehcacheService.findById("2");

        ehcacheService.removeAllUser();

        ehcacheService.findById("1");
        ehcacheService.findById("2");
    }
}
