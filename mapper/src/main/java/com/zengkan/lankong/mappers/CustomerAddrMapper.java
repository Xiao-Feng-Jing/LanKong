package com.zengkan.lankong.mappers;

import com.zengkan.lankong.pojo.CustomerAddr;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zengkan
 * @Date: 2021/07/16/16:33
 * @Description:
 **/
@Repository
@Mapper
public interface CustomerAddrMapper {

    /**
     * 将所有地址设为非默认地址
     * */
    @Update("update customer_addr set is_default = false where customer_id = #{userId}")
    void addressNoDefault(Long userId);

    /**
     * 新增收货地址
     * */
    @Insert("insert into customer_addr(customer_id, name, phone, province, city, district, address, is_default, modified_time) " +
            "values(#{customerId}, #{name}, #{phone}, #{province}, #{city}, #{district}, #{address}, #{isDefault}, #{modifiedTime})")
    @Options(useGeneratedKeys = true, keyProperty = "customerAddrId", keyColumn = "customer_addr_id")
    int saveAddress(CustomerAddr customerAddr);

    /**
     * 更新收货地址
     * */
    @Update("update customer_addr " +
            "set name = #{name}, phone = #{phone}, province = #{province}, city = #{city}, district = #{district}, address = #{address}, is_default = #{isDefault}, modified_time = #{modifiedTime} " +
            "where customer_addr_id = #{customerAddrId}")
    int updateAddressById(CustomerAddr customerAddr);

    /**
     * 删除收货地址
     * */
    @Delete("delete from customer_addr where customer_addr_id = #{id}")
    int deleteAddressById(Long id);

    /**
     * 根据收货地址ID查询地址
     * */
    @Select("select customer_addr_id, customer_id, name, phone, province, city, district, address, is_default, modified_time " +
            "from customer_addr where customer_addr_id = #{addressId}")
    CustomerAddr queryAddressById(Long addressId);

    /**
     * 根据用户ID查询收货地址集合
     * */
    @Select("select customer_addr_id, customer_id, name, phone, province, city, district, address, is_default, modified_time " +
            "from customer_addr where customer_id = #{d}")
    List<CustomerAddr> queryAddressByUserId(Long id);
}
