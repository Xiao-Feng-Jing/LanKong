package com.zengkan.lankong.controller.admin;

import com.zengkan.lankong.pojo.IndexImg;
import com.zengkan.lankong.service.IndexImgService;
import com.zengkan.lankong.enums.CodeEnum;
import com.zengkan.lankong.vo.ResponseBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    private final IndexImgService indexImgService;

    @Autowired
    public IndexImgeController(IndexImgService indexImgService) {
        this.indexImgService = indexImgService;
    }

    @GetMapping("/imageAddr")
    @ApiOperation("获取轮播图")
    public ResponseBean imageAddr(){
        return new ResponseBean(CodeEnum.SUCCESS, indexImgService.queryUrlList());
    }

    @DeleteMapping("/imageAddr")
    @ApiOperation("删除轮播图")
    @RequiresRoles("admin")
    public ResponseBean deleteImageAddr(@RequestParam("id") String id) {
        indexImgService.deleteById(id);
        return new ResponseBean(CodeEnum.DELETE_SUCCESS, null);
    }

    @PostMapping("/imageAddr")
    @ApiOperation("新增轮播图")
    @RequiresRoles("admin")
    public ResponseBean saveImageAddr(@RequestBody IndexImg indexImg){
        return new ResponseBean(CodeEnum.SAVE_SUCCESS, indexImgService.save(indexImg));
    }

    @PutMapping("/imageAddr")
    @ApiOperation("修改轮播图")
    @RequiresRoles("admin")
    public ResponseBean updateImageAddr(@RequestBody IndexImg indexImg){
        return new ResponseBean(CodeEnum.UPDATE_SUCCESS, indexImgService.update(indexImg));
    }

}
