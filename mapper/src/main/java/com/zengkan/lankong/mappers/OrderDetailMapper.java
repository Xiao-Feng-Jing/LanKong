package com.zengkan.lankong.mappers;


import com.zengkan.lankong.pojo.OrderDetail;
import com.zengkan.lankong.vo.CommoditySalesVO;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zengkan
 * @Date: 2021/07/16/16:34
 * @Description:
 **/
@Repository
@Mapper
public interface OrderDetailMapper {

    /**
     * 保存订单详情数据
     * */
    @Insert("<script>insert into order_detail(order_detail_id, order_id, sku_id, title, own_spec, " +
            "product_cnt,product_price,image,modified_time) values " +
            "<foreach collection='orderDetails' item='item' index='index' separator=','> " +
            "(#{item.orderDetailId}, #{item.orderId}, #{item.skuId}, #{item.title}, #{item.ownSpec}, " +
            "#{item.productCnt}, #{item.productPrice}, #{item.image}, #{item.modifiedTime})" +
            "</foreach>" +
            "</script>")
    void saveList(List<OrderDetail> orderDetails);

    /**
     * 根据订单ID查询订单详情
     * @param orderId 订单ID
     * @return 订单详情数据集合
     * */
    @Select("select order_detail_id, order_id, sku_id, title, own_spec, product_cnt, product_price, image from order_detail where order_id = #{orderId} ")
    List<OrderDetail> queryByOrderId(String orderId);

    /**
     * 通过分组查询销量最高的商品
     * */
    @Select("select spu_id, count(*) sales from order_detail where modifiedTime >= #{now} group by spu_id order by sales desc limit 0, #{totals}")
    List<CommoditySalesVO> selectBestSellingGoods(LocalDateTime now, int totals);
}
