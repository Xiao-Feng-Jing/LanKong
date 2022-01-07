package com.zengkan.lankong.mappers;

import com.zengkan.lankong.pojo.CustomerInf;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : ZengKan
 * @version :
 * @Date : 2021/12/16/20:49
 * @Description :
 * @modified By :
 **/
@Repository
@Mapper
public interface CustomerInfMapper {

    @Insert("insert customer_inf(customer_id, customer_name, identity_card_type, identity_card_no, " +
            "mobile_phone, register_time) " +
            "values(#{customerId}, #{customerName}, #{identityCardType}, #{identityCardNo}, " +
            "#{mobilePhone}, #{registerTime})")
    @Options(useGeneratedKeys = true, keyProperty = "customerId", keyColumn = "customer_id")
    void save(CustomerInf customerInf);

    @Update("update customer_inf set customer_name = #{customerName}, " +
            "identity_card_type = #{identityCardType}, identity_card_no = #{identityCardNo}, " +
            "mobile_phone = #{mobilePhone}, customer_email = #{customerEmail}, " +
            "gender = #{gender} where customer_inf_id = ${customerInfId}")
    boolean updateInfo(CustomerInf customerInf);

    @Select("select customer_inf_id, customer_id, customer_name, identity_card_type, identity_card_no, " +
            "mobile_phone, customer_email, gender, register_time, modified_time from customer_inf where customer_id = #{id}")
    CustomerInf selectByUserId(long id);
}
