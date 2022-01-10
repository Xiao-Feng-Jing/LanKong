package com.zengkan.lankong.service.impl;

import com.zengkan.lankong.enums.ExceptionEnum;
import com.zengkan.lankong.exception.MyException;
import com.zengkan.lankong.mappers.CustomerAddrMapper;
import com.zengkan.lankong.pojo.CustomerAddr;
import com.zengkan.lankong.pojo.User;
import com.zengkan.lankong.service.AddressService;
import com.zengkan.lankong.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : ZengKan
 * @version :
  * @Date : 2022/01/07/17:45
 * @Description : 地址管理
 * @modified By :
 **/
@Service
public class AddressServiceImpl implements AddressService {

    private final CustomerAddrMapper customerAddrMapper;
    private final RedisUtil redisUtil;

    @Autowired
    public AddressServiceImpl(CustomerAddrMapper customerAddrMapper, RedisUtil redisUtil) {
        this.customerAddrMapper = customerAddrMapper;
        this.redisUtil = redisUtil;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public CustomerAddr addAddressByUserId(CustomerAddr customerAddr, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        User user = (User) redisUtil.getString(token);
        customerAddr.setCustomerId(user.getId());
        setDefaultAddress(customerAddr);
        if (customerAddrMapper.saveAddress(customerAddr) != 1) {
            throw new MyException(ExceptionEnum.ERROR);
        }
        return customerAddr;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CustomerAddr updateAddressById(CustomerAddr customerAddr) {
        setDefaultAddress(customerAddr);
        if (customerAddrMapper.updateAddressById(customerAddr) != 1){
            throw new MyException(ExceptionEnum.ERROR);
        }
        return customerAddr;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAddressById(Long id) {
        if (customerAddrMapper.deleteAddressById(id) != 1) {
            throw new MyException(ExceptionEnum.ERROR);
        }
    }

    @Override
    public CustomerAddr queryAddressById(Long addressId) {

        return customerAddrMapper.queryAddressById(addressId);
    }

    @Override
    public List<CustomerAddr> queryAddressByUserId(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        User user = (User) redisUtil.getString(token);
        return customerAddrMapper.queryAddressByUserId(user.getId());
    }

    private void setDefaultAddress(CustomerAddr customerAddr) {
        if (customerAddr.isDefault()) {
            customerAddrMapper.addressNoDefault(customerAddr.getCustomerId());
        }
    }
}
