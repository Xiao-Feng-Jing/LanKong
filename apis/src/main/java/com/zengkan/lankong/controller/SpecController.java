package com.zengkan.lankong.controller;

import com.zengkan.lankong.pojo.TbSpecification;
import com.zengkan.lankong.service.SpecService;
import com.zengkan.lankong.enums.CodeEnum;
import com.zengkan.lankong.vo.ResponseBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created with IntelliJ IDEA.
 * @author zengkan
 * @Date: 2021/03/02/18:55
 * @Description:
 **/
@RestController
@RequestMapping("/spec")
@Api(tags = "规格参数")
public class SpecController {

    private final SpecService specService;

    @Autowired
    public SpecController(SpecService specService) {
        this.specService = specService;
    }

    /**
     * 查询商品分类对应的规格参数模板
     * @param id 商品分类 id
     * @return 返回的数据
     * */
    @GetMapping("{id}")
    @RequiresRoles("admin")
    @ApiOperation(value = "查询规格参数模板", notes = "根据分类ID, 权限需要：管理员")
    @ApiImplicitParam(name = "", value = "分类ID", required = true, paramType = "query", dataType = "String")
    public ResponseBean query(@PathVariable("id") long id) {
        return new ResponseBean(CodeEnum.SUCCESS, specService.queryById(id));
    }

    /**
     * 保存一个规格参数模板
     * @param tbSpecification 规格参数模板
     * @return 返回值
     */
    @PostMapping
    @RequiresRoles("admin")
    @ApiOperation(value = "创建规格参数模板", notes = "根据规格参数模板数据模型保存, 权限需要：管理员")
    public ResponseBean saveSpec(@RequestBody TbSpecification tbSpecification){
        specService.save(tbSpecification);
        return new ResponseBean(CodeEnum.SAVE_SUCCESS, null);
    }

    /**
     * 修改一个规格参数模板
     * @param tbSpecification 规格参数模板
     * @return 返回值
     * */
    @PutMapping
    @RequiresRoles("admin")
    @ApiOperation(value = "更新规格参数模板", notes = "根据规格参数模板数据模型更新, 权限需要：管理员")
    public ResponseBean updateSpec(@RequestBody TbSpecification tbSpecification){
        specService.update(tbSpecification);
        return new ResponseBean(CodeEnum.UPDATE_SUCCESS, null);
    }

    /**
     * 删除分类id下的规格参数模板
     * @param cid 分类id
     * @return 返回值
     * */
    @DeleteMapping("/{cid}")
    @RequiresRoles("admin")
    @ApiOperation(value = "删除规格参数模板", notes = "根据分类ID删除, 权限需要：管理员")
    public ResponseBean deleteSpecByCategoryId(@PathVariable("cid") long cid){
        specService.deleteByCategoryId(cid);
        return new ResponseBean(CodeEnum.DELETE_SUCCESS, null);
    }
}
