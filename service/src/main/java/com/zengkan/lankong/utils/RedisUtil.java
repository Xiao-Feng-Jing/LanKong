package com.zengkan.lankong.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zengkan
 * @Date: 2021/01/16/16:34
 * @Description:
 **/
@Component
public class RedisUtil {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    //=============================common============================
    /**
     * 指定缓存失效时间
     * @param key 键 不能为null
     * @param time 时间(秒)
     **/
    public void expire(String key, long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 根据key获取缓存失效时间
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     * */
    public long getExpire(String key) {
        if (key == null || key.length() == 0) {
            return 0L;
        }
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 判断key是否存在
     * @param key 键
     * @return true 存在 false不存在
     **/
    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 删除缓存
     * @param keys 可以传一个值 或多个
     */
    @SuppressWarnings("unchecked")
    public void del(String ... keys) {
        if (keys!=null && keys.length > 0){
            if (keys.length == 1) {
                redisTemplate.delete(keys[0]);
            }else {
                redisTemplate.delete(CollectionUtils.arrayToList(keys));
            }
        }
    }

    //=============================String============================

    /**
     * 普通缓存放入
     * @param key 键 不能为null
     * @param value 值
     */
    public void setString(String key,Object value){
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 普通缓存放入并设置时间
     * @param key 键
     * @param value 值
     * @param time 时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     * */
    public boolean setString(String key, Object value, long time){
        try {
            if (time > 0) {
                this.redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            }else {
                setString(key, value);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 普通缓存获取
     * @param key 键 不能为null
     * @return 值
     * */
    public Object getString(String key){
        return (key==null ? null : redisTemplate.opsForValue().get(key));
    }

    /**
     * 将给定 key 的值设为 value ，并返回 key 的旧值(old value)。
     * @param key 键 不能为null
     * @param value 新值
     * @return 旧值
     * */
    public Object getString(String key,Object value) {
        return (key == null ? null : redisTemplate.opsForValue().getAndSet(key,value));
    }

    //=============================hash============================

    /**
     * 将哈希表 key 中的字段 key1 的值设为 value 。
     * @param key 键
     * @param key1 域 字段
     * @param value 值
     * */
    public void setHash(String key,Object key1, Object value) {
        redisTemplate.opsForHash().put(String.valueOf(key),key1, value);
    }

    /**
     * 将哈希表 key 中的字段 key1 的值设为 value，并设置过期时间
     * @param key 键
     * @param key1 域 字段
     * @param value 值
     * @param time 时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true 成功 false 失败
     * */
    public boolean setHash(String key,Object key1, Object value, long time) {
        try {
            redisTemplate.opsForHash().put(String.valueOf(key), key1, value);
            if (time > 0L){
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 同时将多个 field-value (域-值)对设置到哈希表 key 中。
     * @param key 键
     * @param map 对应的多个域-值
     * @return true 成功 false 失败
     * */
    public boolean hmSet(String key, Map<String, Object> map){
        try {
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 同时将多个 field-value (域-值)对设置到哈希表 key 中，并设置过期时间。
     * @param key 键
     * @param map 对应的多个域-值
     * @param time 时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true 成功 false 失败
     * */
    public boolean hmSet(String key, Map<Object, Object> map,long time) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            if (time > 0L){
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 判断hash表中是否有该域的值
     * @param key 键 不能为null
     * @param key1 域 不能为null
     * @return true 存在 false不存在
     */
    public boolean hasKey(String key,Object key1){
        try {
            redisTemplate.opsForHash().hasKey(key, key1);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回，by为整数
     * @param key 键
     * @param item 域
     * @param by 要增加几(大于0)
     * @return 新增后的值
     */
    public Long hincr(String key, Object item, long by){
        return redisTemplate.opsForHash().increment(key,item,by);
    }

    /**
     * hash递减 如果不存在,就会创建一个 并把新增后的值返回，by为整数
     * @param key 键
     * @param item 域
     * @param by 要减少记(小于0)
     * @return 递减后的值
     */
    public Long hdecr(String key, Object item, long by){
        return redisTemplate.opsForHash().increment(key,item,-by);
    }

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回，by为浮点数
     * @param key 键
     * @param item 域
     * @param by 要增加几(大于0)
     * @return 新增后的值
     */
    public double hincr(String key, Object item, double by){
        return redisTemplate.opsForHash().increment(key, item, by);
    }

    /**
     * hash递减 如果不存在,就会创建一个 并把新增后的值返回，by为浮点数
     * @param key 键
     * @param item 域
     * @param by 要减少记(小于0)
     * @return 递减后的值
     */
    public double hdecr(String key, Object item, double by){
        return redisTemplate.opsForHash().increment(key, item, -by);
    }

    /**
     * HashGet
     * @param key 键 不能为null
     * @param key1 域 不能为null
     * @return 值
     */
    public Object hGet(String key, Object key1){
        return redisTemplate.opsForHash().get(key,key1);
    }


    /**
     * 根据key获取Set中的所有域
     * @param key 键 不能为null
     * @return 域
     */
    public Set<Object> hGet(String key){
        return redisTemplate.opsForHash().keys(key);
    }

    /**
     * 根据key获取Set中的所有值
     * @param key 键 不能为null
     * @return 值
     */
    public List<Object> hGetValues(String key){
        return redisTemplate.opsForHash().values(key);
    }

    /**
     * 获取hashKey对应的所有域值
     * @param key 键
     * @return 对应的多个域值
     */
    public Map<Object, Object> hmGet(String key){
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 删除hash表中的值
     * @param key 键 不能为null
     * @param item 域 可以是多个 不能为null
     */
    public void hDelete(String key, Object... item){
        redisTemplate.opsForHash().delete(key,item);
    }

    /**
     * 通过键获取哈希值
     * @param key 键
     * @return 哈希值
     * */
    public Long hSize(String key){
        return redisTemplate.opsForHash().size(key);
    }

    //=============================list============================

    /**
     * 将list放入缓存头部
     * @param key 键
     * @param list 值
     * @return true 成功 false 失败
     */
    public boolean lPush(String key, List<String> list){
        Long n = null;
        try {
            n=redisTemplate.opsForList().leftPushAll(key, list);
            return n != null;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将value放入缓存头部
     * @param key 键
     * @param value 值
     * @return true 成功 false 失败
     */
    public boolean lPushX(String key, String value){
        Long n = null;
        try {
            n=redisTemplate.opsForList().leftPush(key, value);
            return n != null;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将list放入缓存
     * @param key 键
     * @param list 值
     * @return true 成功 false 失败
     */
    public boolean rPush(String key, List<String> list) {
        Long n = null;
        try {
            n=redisTemplate.opsForList().rightPushAll(key, list);
            return n != null;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将value放入缓存
     * @param key 键
     * @param value 值
     * @return true 成功 false 失败
     */
    public boolean rPushX(String key, String value) {
        Long n = null;
        try {
            n=redisTemplate.opsForList().rightPush(key, value);
            return n != null;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据索引修改list中的某条数据
     * @param key 键
     * @param index 索引
     * @param value 值
     */
    public void lUpdatePush(String key,int index,String value){
        redisTemplate.opsForList().set(key, index, value);
    }

    /**
     * 移出并获取列表的第一个元素
     * @param key 键
     * @return 值
     */
    public Object lGetPop(String key) {
        return redisTemplate.opsForList().leftPop(key);
    }

    /**
     * 移出并获取列表的第一个元素，如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止。
     * @param key 键
     * @return 值
     */
    public Object lGetPop(String key , long timeout) {
        return redisTemplate.opsForList().leftPop(key,timeout,TimeUnit.SECONDS);
    }

    /**
     * 移出并获取列表的最后一个元素。
     * @param key 键
     * @return 值
     */
    public Object rGetPop(String key) {
        return redisTemplate.opsForList().rightPop(key);
    }

    /**
     * 移出并获取列表的最后一个元素，如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止。
     * @param key 键
     * @return 值
     */
    public Object rGetPop(String key , long timeout) {
        return redisTemplate.opsForList().rightPop(key, timeout, TimeUnit.SECONDS);
    }

    /**
     * 通过索引 获取list中的值
     * @param key 键
     * @param index 索引  index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return 值
     */
    public Object lGetIndex(String key,long index){
        return redisTemplate.opsForList().index(key, index);
    }

    /**
     * 获取list缓存的内容
     * @param key 键
     * @param start 开始
     * @param end 结束  0 到 -1代表所有值
     * @return 多个值
     */
    public List<Object> lGet(String key, long start, long end){
        return redisTemplate.opsForList().range(key, start, end);
    }

    /**
     * 获取list缓存的长度
     * @param key 键
     * @return 长度
     */
    public Long lSize(String key){
        return redisTemplate.opsForList().size(key);
    }

    /**
     * 移除N个值为value
     * @param key 键
     * @param count 移除多少个
     * @param value 值
     * @return 移除的个数
     */
    public long lRemove(String key,long count,Object value) {
        try {
            Long n = redisTemplate.opsForList().remove(key, count, value);
            if (n == null) {
                return 0;
            }
            return n;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 对一个列表进行修剪(trim)，就是说，让列表只保留指定区间内的元素，不在指定区间之内的元素都将被删除。
     * @param key 键 不能为null
     * @param start 开始
     * @param end 结束  0 到 -1代表所有值
     * */
    public void lTrim(String key,long start, long end){
        redisTemplate.opsForList().trim(key, start, end);
    }

    //=============================set============================

    /**
     * 将数据放入set缓存
     * @param key 键
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public long sSet(String key, Object ... values){
        try {
            Long n = redisTemplate.opsForSet().add(key,values);
            if (n == null) {
                return 0;
            }
            return n;
        }catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 将set数据放入缓存
     * @param key 键
     * @param time 时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public long sSet(String key,long time, Object ... values){
        try {
            Long n = redisTemplate.opsForSet().add(key,values);
            if (n == null) {
                return 0;
            }
            if (time > 0){
                expire(key, time);
            }
            return n;
        }catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 移除值为value的
     * @param key 键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    public long sRemove(String key, Object ...values){
        try {
            Long n = redisTemplate.opsForSet().remove(key,values);
            if (n == null) {
                return 0;
            }
            return n;
        }catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 移除并返回集合中的一个随机元素
     * @param key 键
     * @return 值
     * */
    public Object sPop(String key){
        return redisTemplate.opsForSet().pop(key);
    }

    /**
     * 移除并返回集合中的一个或多个随机元素
     * @param key 键
     * @return 值
     * */
    public List<Object> sPop(String key,long count){
        return redisTemplate.opsForSet().pop(key, count);
    }

    /**
     * 获取set缓存的长度
     * @param key 键
     * @return 长度
     */
    public Long sSize(String key){
        return redisTemplate.opsForSet().size(key);
    }

    /**
     * 返回给定所有集合的差集
     * @param key1 集合1
     * @param key2 集合2
     * @return 差集
     * */
    public Set<Object> sDifference(String key1,String key2){
        return redisTemplate.opsForSet().difference(key1, key2);
    }

    /**
     * 返回给定所有集合的交集
     * @param key1 集合1
     * @param key2 集合2
     * @return 交集
     * */
    public Set<Object> sIntersect(String key1, String key2){
        return redisTemplate.opsForSet().intersect(key1,key2);
    }

    /**
     * 所有给定集合的并集
     * @param key1 集合1
     * @param key2 集合2
     * @return 并集
     * */
    public Set<Object> sUnion(String key1,String key2){
        return redisTemplate.opsForSet().union(key1,key2);
    }

    /**
     * 返回集合中一个或多个随机数
     * @param key 键
     * @param count 个数
     * @return 值的集合
     * */
    public List<Object> sRemoveRanges(String key,long count){
        return redisTemplate.opsForSet().randomMembers(key, count);
    }

    /**
     * 返回集合中一个随机数
     * @param key 键
     * @return 值
     * */
    public Object sRemoveRange(String key){
        return redisTemplate.opsForSet().randomMember(key);
    }

    /**
     * 根据key获取Set中的所有值
     * @param key 键
     * @return 所有值
     */
    public Set<Object> sMembers(String key){
        return redisTemplate.opsForSet().members(key);
    }

    /**
     * 根据value从一个set中查询,是否存在
     * @param key 键
     * @param value 值
     * @return true 存在 false不存在
     */
    public Boolean isMember(String key,Object value){
        return redisTemplate.opsForSet().isMember(key,value);
    }

    //=============================sorted set============================

    /**
     * 实现添加一个成员
     * @param key 键
     * @param value 值
     * @param score 分数
     */
    public void zSet(String key, Object value,double score){
        redisTemplate.opsForZSet().add(key, value, score);
    }

    /**
     * 向有序集合添加一个或多个成员，或者更新已存在成员的分数
     * @param key 键
     * @param value 值
     */
    public void zSet(String key, Set<ZSetOperations.TypedTuple<Object>> value){
        redisTemplate.opsForZSet().add(key, value);
    }

    /**
     * 计算在有序集合中指定区间分数的成员数
     * @param key 键 不能为null
     * @param min 最小分数
     * @param max 最大分数
     * @return 成员数
     * */
    public Long zCount(String key, double min,double max){
        return redisTemplate.opsForZSet().count(key,min, max);
    }

    /**
     * 返回使用给定的{@code key}存储的排序集中的大小
     * @param key 键
     * @return 大小
     * */
    public Long zSize(String key){
        return redisTemplate.opsForZSet().size(key);
    }

    /**
     * 返回使用给定的{@code key}存储的排序集中的大小
     * @param key 键
     * @return 元素个数
     */
    public Long zCard(String key){
        return redisTemplate.opsForZSet().zCard(key);
    }

    /**
     * 从排序集中获取{@code start}和{@code end}之间的元素。
     * @param key 键
     * @param start 开始
     * @param end 结束
     * @return 元素集合
     */
    public Set<Object> zRanges(String key,long start, long end){
        return redisTemplate.opsForZSet().range(key,start, end);
    }

    /**
     * 从高到低排序的集合中，获取{@code start}到{@code end}范围内的元素。
     * @param key 键
     * @param start 开始
     * @param end 结束
     * @return 元素集合
     */
    public Set<Object> zReverseRanges(String key, long start, long end){
        return redisTemplate.opsForZSet().reverseRange(key, start, end);
    }

    /**
     * 实现命令 : ZSCORE key value
     * 获取成员的分数
     *
     * @param key 键
     * @param value 值
     * @return 分数
     */
    public Double zScore(String key,Object value){
        return redisTemplate.opsForZSet().score(key, value);
    }

    /**
     * 实现命令 : ZINCRBY key 带符号的双精度浮点数 value
     * 增减成员的分数
     *
     * @param key 键 不能为null
     * @param value 值
     * @param delta 带符号的双精度浮点数
     * @return 修改后的值
     */
    public Double zIncrBy(String key,Object value,double delta){
        return redisTemplate.opsForZSet().incrementScore(key, value,delta);
    }

    /**
     * 根据key和value删除元素
     * @param key 键
     * @param value 值
     * @return 已删除元素的数量。
     */
    public Long zRemove(String key,Object... value){
        return redisTemplate.opsForZSet().remove(key,value);
    }

    /**
     * 返回元素在集合的排名,有序集合是按照元素的score值由小到大排列
     * @param key 键
     * @param value 值
     * @return 索引
     * */
    public Long zRank(String key,Object value){
        return redisTemplate.opsForZSet().rank(key,value);
    }

    /**
     * 返回元素在集合的排名,有序集合是按照元素的score值由大到小排列
     * @param key 键
     * @param value 值
     * @return 索引
     * */
    public Long zRevRank(String key,Object value){
        return redisTemplate.opsForZSet().reverseRank(key,value);
    }

    //=============================HyperLogLog============================

    /**
     * 将指定的元素添加到HyperLogLog中，可以添加多个元素
     * @param key 键
     * @param values 值
     * @return 至少有个元素被添加返回 1， 否则返回 0。
     * */
    public Long hyperLogAdd(String key,Object ... values){
        return redisTemplate.opsForHyperLogLog().add(key,values);
    }

    /**
     *返回给定HyperLogLog的基数估算值。当一次统计多个HyperLogLog时(通过key实现时间窗口统计user:web1:2020061600、user:web1:2020061601)，需要对多个HyperLogLog结构进行比较，并将并集的结果放入一个临时的HyperLogLog，性能不高，谨慎使用
     * @param keys 一个或多个键
     * @return 元素个数
     * */
    public Long hyperLogSize(String ... keys){
        return redisTemplate.opsForHyperLogLog().size(keys);
    }

    /**
     * 将多个HyperLogLog进行合并，将并集的结果放入一个指定的HyperLogLog中
     * @param key 合并后的键
     * @param keys 多个键 合并前的键
     * @return 合并后的元素个数
     * */
    public Long hyperLogUnions(String key,String ... keys){
        return redisTemplate.opsForHyperLogLog().union(key, keys);
    }

    /**
     *删除给定的键
     * @param key 键
     * */
    public void hyperLogDel(String key){
        redisTemplate.opsForHyperLogLog().delete(key);
    }

    //=============================redis============================

    /**
     * 加锁
     * @param key 键
     * @param value 值
     * @return true 成功 false 失败
     * */
    public Boolean lock(String key, Object value){
        // setIfAbsent相当于jedis中的setnx，如果能赋值就返回true，如果已经有值了，就返回false
        // 即：在判断这个key是不是第一次进入这个方法
        //第一次，即:这个key还没有被赋值的时候
        return redisTemplate.opsForValue().setIfAbsent(key, value, Duration.ofMillis(1L));
    }

    /**
     * 解锁
     * @param key 键
     * @param value 值
     * */
    public void unlock(String key, Object value){
        try{
            if(Objects.equals(redisTemplate.opsForValue().get(key), value)){
                redisTemplate.opsForValue().getOperations().delete(key);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
