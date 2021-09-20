package com.zengkan.lankong.controller.admin;

import com.zengkan.lankong.pojo.IndexImg;
import com.zengkan.lankong.service.IndexImgService;
import com.zengkan.lankong.vo.ResponseBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zengkan
 * @Date: 2021/04/19/21:58
 * @Description:
 **/
@RestController
@RequestMapping("manage/index")
@Api(tags = "首页轮播图管理")
public class IndexImgeController {

    private static final String SUCCESS_MESSAGE = "SUCCESS";
    private static final String FAIL_MESSAGE = "FAIL";

    private final IndexImgService carouselMapService;

    @Autowired
    public IndexImgeController(IndexImgService indexImgService) {
        this.carouselMapService = indexImgService;
    }

    @GetMapping("/imageAddr")
    @ApiOperation("图片地址的获取")
    public ResponseBean imageAddr(){
        return new ResponseBean(200,SUCCESS_MESSAGE,carouselMapService.queryUrlList());
    }

    @DeleteMapping("/imageAddr")
    @ApiOperation("删除轮播图")
    @RequiresRoles("admin")
    public ResponseBean deleteImageAddr(@RequestParam("id") String id) {
        if (carouselMapService.deleteById(id)){
            return new ResponseBean(200,SUCCESS_MESSAGE,null);
        }
        return new ResponseBean(404,"轮播图不存在",null);
    }

    @PostMapping("/imageAddr")
    @ApiOperation("新增轮播图")
    @RequiresRoles("admin")
    public ResponseBean saveImageAddr(@RequestBody IndexImg indexImg){
        return new ResponseBean(201,SUCCESS_MESSAGE,carouselMapService.save(indexImg));
    }

    @PutMapping("/imageAddr")
    @ApiOperation("修改轮播图")
    @RequiresRoles("admin")
    public ResponseBean updateImageAddr(@RequestBody IndexImg indexImg){
        return new ResponseBean(201,SUCCESS_MESSAGE,carouselMapService.update(indexImg));
    }

}
