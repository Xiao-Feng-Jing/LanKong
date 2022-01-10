package com.zengkan.lankong.controller;

import com.zengkan.lankong.enums.CodeEnum;
import com.zengkan.lankong.pojo.GoodsSku;
import com.zengkan.lankong.service.GoodsService;
import com.zengkan.lankong.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zengkan
 * @Date: 2021/03/02/20:15
 * @Description:
 **/
@RestController
@RequestMapping("/goods")
@Api(tags = "商品管理")
public class GoodsController {

    private final GoodsService goodsService;

    @Autowired
    public GoodsController(GoodsService goodsService) {
        this.goodsService = goodsService;
    }

    /**
     * 分页查询
     * @param pageNum 页号
     * @param rows 每页大小
     * @param key 搜索关键字
     * @param saleable 是否上架
     * @param isNew 是否新品
     * @return 返回结果集
     */
    @GetMapping("/spu/page")
    @RequiresRoles("admin")
    @ApiOperation(value = "后台分页查询商品", notes = "根据页条件查询,，后台显示商品接口，权限需要：管理员")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页号", required = true, defaultValue = "1", paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "rows", value = "每页大小", required = true, defaultValue = "5", paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "key", value = "搜索关键字", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "saleable", value = "是否上架", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "isNew", value = "是否新品", required = false, paramType = "query", dataType = "String")
    })
    public ResponseBean pageList(
            @RequestParam(value = "pageNum", defaultValue = "1", required = true) Integer pageNum,
            @RequestParam(value = "rows", defaultValue = "5", required = true) Integer rows,
            @RequestParam(value="key", required = false)  String key,
            @RequestParam(value="saleable", required = false) String saleable,
            @RequestParam(value="isNew", required = false) String isNew) {
        SpuQueryByPage spuQueryByPage = new SpuQueryByPage(pageNum, rows, key, saleable, isNew);
        PageResult<SpuVO> result = goodsService.pageList(spuQueryByPage);
        return new ResponseBean(CodeEnum.SUCCESS, result);
    }

    /**
     * 保存商品
     * @param spuVo 商品信息
     * */
    @PostMapping("/spu")
    @RequiresRoles("admin")
    @ApiOperation(value = "添加商品信息", notes = "前端提供数据模型, 权限需要：管理员")
    @ApiImplicitParam(name = "spuVo", value = "商品数据模型")
    public ResponseBean saveGoods(@RequestBody SpuVO spuVo){
        this.goodsService.saveGoods(spuVo);
        return new ResponseBean(CodeEnum.SAVE_SUCCESS, null);
    }

    /**
     * 修改商品
     * @param spuVo 商品信息
     * */
    @PutMapping("/spu")
    @RequiresRoles("admin")
    @ApiOperation(value = "修改商品信息", notes = "前端提供数据模型, 权限需要：管理员")
    @ApiImplicitParam(name = "spuVo", value = "商品数据模型")
    public ResponseBean updateGoods(@RequestBody SpuVO spuVo){
        this.goodsService.updateGoods(spuVo);
        return new ResponseBean(CodeEnum.UPDATE_SUCCESS, null);
    }

    /**
     * 根据id查询商品
     * @param spuId spuId
     * */
    @GetMapping("/spu")
    @ApiOperation(value = "查询商品", notes = "根据spuID查询, 权限需要：无")
    @ApiImplicitParam(name = "spuId", value = "商品id",required = true, paramType = "query", dataType = "String")
    public ResponseBean querySpu(@RequestParam("spuId") String spuId) {
        return new ResponseBean(CodeEnum.SUCCESS, this.goodsService.querySpuById(spuId));
    }

    /**
     * 根据spu id查询所有的sku
     * @param spuId spuId
     * @return sku集合
     * */
    @GetMapping("/spu/list")
    @ApiOperation(value = "查询sku", notes = "根据spuID查询, 权限需要：无")
    @ApiImplicitParam(name = "spuId", value = "商品ID", required = true, paramType = "query", dataType = "String")
    public ResponseBean querySpuList(@RequestParam("spuId") String spuId){
        List<GoodsSku> goodsSkuList = this.goodsService.querySkuBySpuId(spuId);
        return new ResponseBean(CodeEnum.SUCCESS, goodsSkuList);
    }

    /**
     * 根据spu id查询商品详情
     * @param spuId spuId
     * @return sku集合
     * */
    @GetMapping("/spu/detail")
    @ApiOperation(value = "商品详情", notes = "根据spuID查询, 权限需要：无")
    @ApiImplicitParam(name = "spuId", value = "商品id", required = true, paramType = "query", dataType = "String")
    public ResponseBean querySpuDetail(@RequestParam("spuId") String spuId) {
        return new ResponseBean(CodeEnum.SUCCESS, this.goodsService.querySpuDetailBySpuId(spuId));
    }

    /**
     * 商品上下架
     * @param spuIds 商品id集合
     * */
    @GetMapping("/spu/saleable")
    @RequiresRoles("admin")
    @ApiOperation(value = "商品上下架", notes = "根据spuID查询, 权限需要：管理员")
    @ApiImplicitParam(name = "spuIds", value = "商品id集合", required = true, paramType = "query", dataType = "String")
    public ResponseBean goodsSoldOut(@RequestParam("spuIds") List<String> spuIds) {
        this.goodsService.goodsSoldOut(spuIds);
        return new ResponseBean(CodeEnum.UPDATE_SUCCESS, null);
    }

    /**
     * 删除商品
     * @param ids 商品id集合
     * */
    @DeleteMapping("/spu")
    @RequiresRoles("admin")
    @ApiOperation(value = "批量删除商品", notes = "根据spuID删除, 权限需要：管理员")
    @ApiImplicitParam(name = "ids", value = "商品spuId集合", required = true, paramType = "from", dataType = "List")
    public ResponseBean deleteGoods(@RequestParam("ids") List<String> ids) {
        this.goodsService.deleteGoods(ids);
        return new ResponseBean(CodeEnum.DELETE_SUCCESS, null);
    }

    /**
     * 根据id查询商品sku
     * @param skuId skuId
     * */
    @GetMapping("/sku")
    @ApiOperation(value = "查询SKU", notes = "根据SKUID查询, 权限需要：无")
    @ApiImplicitParam(name = "skuId", value = "商品skuId", required = true, paramType = "query", dataType = "String")
    public ResponseBean goodsSku(@RequestParam("skuId") String skuId) {
        return new ResponseBean(CodeEnum.SUCCESS, this.goodsService.querySkuBySkuId(skuId));
    }

    /**
     * 展示购物车页时发起的查询,用于检查商品最新价格、是否下架、库存
     * */
    @PostMapping("/sku/list")
    @ApiOperation(value = "批量查询SKU", notes = "根据SKUID查询, 权限需要：无")
    @ApiImplicitParam(name = "ids",value = "商品skuId集合", required = true, paramType = "from", dataType = "String")
    public ResponseBean goodsSkus(@RequestParam("ids") List<String> ids) {
        return new ResponseBean(CodeEnum.SUCCESS, this.goodsService.querySkuBySkuIds(ids));
    }

}
