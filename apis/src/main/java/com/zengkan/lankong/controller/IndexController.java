package com.zengkan.lankong.controller;

import com.zengkan.lankong.enums.CodeEnum;
import com.zengkan.lankong.service.IndexImageService;
import com.zengkan.lankong.service.IndexService;
import com.zengkan.lankong.vo.ResponseBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : ZengKan
 * @version :
 * @Date : 2021/09/26/16:45
 * @Description : 首页门户
 * @modified By :
 **/
@RestController
@RequestMapping("/index")
@Api(value = "提供前台数据显示所需的接口",tags = "商城前台数据")
public class IndexController {

    private final IndexService indexService;
    private final IndexImageService indexImageService;

    @Autowired
    public IndexController(IndexService indexService, IndexImageService indexImageService) {
        this.indexService = indexService;
        this.indexImageService = indexImageService;
    }


    /**
     * 首页获取轮播图
     * */
    @GetMapping("/banner")
    @ApiOperation(value = "获取轮播图", notes = "获取轮播图，权限需要：无")
    public ResponseBean listIndexImages() {
        return new ResponseBean(CodeEnum.SUCCESS, indexImageService.listIndexImages());
    }

    /**
     * 首页推荐分类显示
     * */
    @GetMapping("/category")
    @ApiOperation(value = "显示推荐分类", notes = "获取所有的推荐，权限需要：无")
    public ResponseBean listCategoryVO() {
        return new ResponseBean(CodeEnum.SUCCESS, indexService.listCategoryVO());
    }

    /**
     * 获取热门商品数据
     * */
    @GetMapping("/popular")
    @ApiOperation(value = "显示热销商品", notes = "获取热销商品，权限需要：无")
    public ResponseBean listPopularProducts() {
        return new ResponseBean(CodeEnum.SUCCESS, indexService.listBestSellingGoods());
    }

    /**
     * 获取精品商品数据
     * */
    @GetMapping("/boutique")
    @ApiOperation(value = "显示精品商品", notes = "获取精品商品，权限需要：无")
    public ResponseBean listBoutiqueProducts() {
        return new ResponseBean(CodeEnum.SUCCESS, indexService.listBoutiqueGoods());
    }

    /**
     * 获取热搜关键词
     * */
    @GetMapping("/searchKey")
    @ApiOperation(value = "显示热搜关键词", notes = "获取热搜关键词，权限需要：无")
    public ResponseBean listSearchKey() {
        return new ResponseBean(CodeEnum.SUCCESS, indexService.listSearchKey());
    }



    /**
     * 根据分类查询商品信息
     * */
    @GetMapping("/list/{cid}")
    @ApiOperation(value = "根据分类查询商品SPU", notes = "通过分类ID查询商品并分页，权限需要：无")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cid", value = "分类ID", required = true, paramType = "query", dataType = "Long"),
            @ApiImplicitParam(name = "pageNum", value = "页号", required = true, defaultValue = "1", paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "rows", value = "每页大小", required = true, defaultValue = "12", paramType = "query", dataType = "Integer")
    })
    public ResponseBean goodsSpuByCategoryId(@PathVariable("cid") Long cid,
                                             @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                             @RequestParam(value = "rows", defaultValue = "12") Integer rows) {
        return new ResponseBean(CodeEnum.SUCCESS, this.indexService.querySpuByCategoryId(cid, pageNum, rows));
    }

    /**
     * 根据关键字查询商品信息
     * */
    @GetMapping("/search")
    @ApiOperation(value = "根据关键字查询商品SPU", notes = "通过搜索关键字查询商品并分页，权限需要：无")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "key", value = "搜索关键字", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "pageNum", value = "页号", required = true, defaultValue = "1", paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "rows", value = "每页大小", required = true, defaultValue = "12", paramType = "query", dataType = "Integer")
    })
    public ResponseBean searchGoodsSpu(@RequestParam(value = "key", required = true) String key,
                                       @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                       @RequestParam(value = "rows", defaultValue = "12") Integer rows) {
        return new ResponseBean(CodeEnum.SUCCESS, this.indexService.searchGoodsSpu(key, pageNum, rows));
    }
}
