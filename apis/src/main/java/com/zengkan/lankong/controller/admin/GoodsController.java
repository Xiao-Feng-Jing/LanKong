package com.zengkan.lankong.controller.admin;

import com.zengkan.lankong.enums.CodeEnum;
import com.zengkan.lankong.pojo.GoodsSku;
import com.zengkan.lankong.pojo.SpuDetail;
import com.zengkan.lankong.service.GoodsService;
import com.zengkan.lankong.vo.*;
import io.swagger.annotations.Api;
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
@RequestMapping("manage/goods")
@Api(tags = "商品管理")
public class GoodsController {

    private final GoodsService goodsService;

    @Autowired
    public GoodsController(GoodsService goodsService) {
        this.goodsService = goodsService;
    }

    /**
     * 分页查询
     * @param page 页数
     * @param rows 每页大小
     * @param search 搜索关键字
     * @param saleable 是否上架
     * @return 返回结果集
     */
    @GetMapping("/spu/page")
    public ResponseBean pageList(
            @RequestParam(value = "page", defaultValue = "1", required = true) Integer page,
            @RequestParam(value = "row", defaultValue = "5", required = true) Integer rows,
            @RequestParam(value="search", required = false)  String search,
            @RequestParam(value="saleable", required = false) String saleable,
            @RequestParam(value="isNew", required = false) String isNew) {
        SpuQueryByPage spuQueryByPage = new SpuQueryByPage(page, rows,search,saleable,isNew);
        PageResult<SpuVo> result = goodsService.pageList(spuQueryByPage);
        return new ResponseBean(CodeEnum.SUCCESS, result);
    }

    /**
     * 保存商品
     * @param spuVo 商品信息
     * */
    @PostMapping("/spu")
    @RequiresRoles("admin")
    public ResponseBean saveGoods(@RequestBody SpuVo spuVo){
        this.goodsService.saveGoods(spuVo);
        return new ResponseBean(CodeEnum.SAVE_SUCCESS, null);
    }

    /**
     * 修改商品
     * @param spuVo 商品信息
     * */
    @PutMapping("/spu")
    @RequiresRoles("admin")
    public ResponseBean updateGoods(@RequestBody SpuVo spuVo){
        this.goodsService.updateGoods(spuVo);
        return new ResponseBean(CodeEnum.UPDATE_SUCCESS, null);
    }

    /**
     * 根据id查询商品
     * @param spuId spuId
     * */
    @GetMapping("/spu/{id}")
    public ResponseBean querySpu(@PathVariable("id") String spuId) {
        return new ResponseBean(CodeEnum.SUCCESS, this.goodsService.querySpuById(spuId));
    }

    /**
     * 根据spu id查询所有的sku
     * @param spuId spuId
     * @return sku集合
     * */
    @GetMapping("/spu/list/{id}")
    public ResponseBean querySpuList(@PathVariable("id") String spuId){
        List<GoodsSku> goodsSkuList = this.goodsService.querySkuBySpuId(spuId);
        return new ResponseBean(CodeEnum.SUCCESS, goodsSkuList);
    }

    /**
     * 根据spu id查询商品详情
     * @param spuId spuId
     * @return sku集合
     * */
    @GetMapping("/spu/detail/{id}")
    public ResponseBean querySpuDetail(@PathVariable("id") String spuId) {
        return new ResponseBean(CodeEnum.SUCCESS, this.goodsService.querySpuDetailBySpuId(spuId));
    }

    /**
     * 商品上下架
     * @param spuId 商品id
     * */
    @GetMapping("/spu/out/{id}")
    @RequiresRoles("admin")
    public ResponseBean goodsSoldOut(@PathVariable("id") String spuId) {
        this.goodsService.goodsSoldOut(spuId);
        return new ResponseBean(CodeEnum.UPDATE_SUCCESS, null);
    }

    /**
     * 删除商品
     * @param ids 商品id集合
     * */
    @DeleteMapping("/spu")
    @RequiresRoles("admin")
    public ResponseBean deleteGoods(@RequestParam("ids") List<String> ids) {
        this.goodsService.deleteGoods(ids);
        return new ResponseBean(CodeEnum.DELETE_SUCCESS, null);
    }

    /**
     * 根据id查询商品sku
     * @param skuId skuId
     * */
    @GetMapping("/sku/{id}")
    public ResponseBean goodsSku(@PathVariable("id") String skuId) {
        return new ResponseBean(CodeEnum.SUCCESS, this.goodsService.querySkuBySkuId(skuId));
    }
}
