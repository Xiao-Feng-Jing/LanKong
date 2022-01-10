package com.zengkan.lankong.controller;

import com.zengkan.lankong.enums.CodeEnum;
import com.zengkan.lankong.pojo.CustomerAddr;
import com.zengkan.lankong.service.AddressService;
import com.zengkan.lankong.vo.ResponseBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : ZengKan
 * @version :
 * @Date : 2022/01/07/17:30
 * @Description : 收货地址管理
 * @modified By :
 **/
@RestController
@RequestMapping("/address")
@Api(tags = "地址管理")
public class AddressController {

    private final AddressService addressService;

    @Autowired
    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @PostMapping
    @RequiresRoles(value = {
        "user"
    })
    @ApiOperation(value = "创建收货地址", notes = "角色：用户")
    @ApiImplicitParam(name = "customerAddr", value = "收货地址数据模型")
    public ResponseBean addAddress(@RequestBody @Valid CustomerAddr customerAddr, HttpServletRequest request) {
        return new ResponseBean(CodeEnum.SUCCESS, addressService.addAddressByUserId(customerAddr, request));
    }

    @PutMapping
    @RequiresRoles(value = {
            "user"
    })
    @ApiOperation(value = "修改收货地址", notes = "角色：用户")
    @ApiImplicitParam(name = "customerAddr", value = "收货地址数据模型")
    public ResponseBean updateAddress(@RequestBody @Valid CustomerAddr customerAddr) {
        return new ResponseBean(CodeEnum.SUCCESS, addressService.updateAddressById(customerAddr));
    }

    @DeleteMapping("{addressId}")
    @RequiresRoles(value = {"user"})
    @ApiOperation(value = "删除收货地址", notes = "角色：用户")
    @ApiImplicitParam(name = "addressId", value = "收货地址ID")
    public ResponseBean deleteAddress(@PathVariable("addressId") Long addressId) {
        addressService.deleteAddressById(addressId);
        return new ResponseBean(CodeEnum.SUCCESS, null);
    }

    @GetMapping("{addressId}")
    @RequiresRoles(value = {"user"})
    @ApiOperation(value = "根据地址id查询收货地址", notes = "角色：用户")
    @ApiImplicitParam(name = "addressId", value = "收货地址ID")
    public ResponseBean queryAddressById(@PathVariable("addressId") Long addressId) {
        return new ResponseBean(CodeEnum.SUCCESS, addressService.queryAddressById(addressId));
    }

    @GetMapping()
    @RequiresRoles(value = {"user"})
    @ApiOperation(value = "查询收货地址, 返回地址列表", notes = "角色：用户")
    public ResponseBean queryAddressByUserId(HttpServletRequest request) {
        return new ResponseBean(CodeEnum.SUCCESS, addressService.queryAddressByUserId(request));
    }
}
