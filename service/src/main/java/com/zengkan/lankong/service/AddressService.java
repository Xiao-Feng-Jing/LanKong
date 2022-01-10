package com.zengkan.lankong.service;

import com.zengkan.lankong.pojo.CustomerAddr;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : ZengKan
 * @version :
 * @Date : 2022/01/07/17:45
 * @Description :
 * @modified By :
 **/
public interface AddressService {
    CustomerAddr addAddressByUserId(CustomerAddr customerAddr, HttpServletRequest request);

    void deleteAddressById(Long id);

    CustomerAddr queryAddressById(Long addressId);

    List<CustomerAddr> queryAddressByUserId(HttpServletRequest request);

    Object updateAddressById(CustomerAddr customerAddr);
}
