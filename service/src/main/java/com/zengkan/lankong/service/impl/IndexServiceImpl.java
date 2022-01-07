package com.zengkan.lankong.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zengkan.lankong.enums.ExceptionEnum;
import com.zengkan.lankong.exception.MyException;
import com.zengkan.lankong.mappers.*;
import com.zengkan.lankong.pojo.CategoryRecommends;
import com.zengkan.lankong.pojo.GoodsCategory;
import com.zengkan.lankong.pojo.GoodsSku;
import com.zengkan.lankong.pojo.WarehouseGoods;
import com.zengkan.lankong.service.IndexService;
import com.zengkan.lankong.utils.RedisUtil;
import com.zengkan.lankong.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : ZengKan
 * @version :
 * @Date : 2021/09/26/16:50
 * @Description : 首页数据管理
 * @modified By :
 **/
@Service
public class IndexServiceImpl implements IndexService {

    private final CategoryRecommendsMapper categoryRecommendsMapper;
    private final CategoryMapper categoryMapper;
    private final GoodsSpuMapper goodsSpuMapper;
    private final OrderDetailMapper orderDetailMapper;
    private final GoodsSkuMapper goodsSkuMapper;
    private final RedisUtil redisUtil;

    @Autowired
    public IndexServiceImpl(CategoryRecommendsMapper categoryRecommendsMapper, CategoryMapper categoryMapper, GoodsSpuMapper goodsSpuMapper, OrderDetailMapper orderDetailMapper, GoodsSkuMapper goodsSkuMapper, RedisUtil redisUtil) {
        this.categoryRecommendsMapper = categoryRecommendsMapper;
        this.categoryMapper = categoryMapper;
        this.goodsSpuMapper = goodsSpuMapper;
        this.orderDetailMapper = orderDetailMapper;
        this.goodsSkuMapper = goodsSkuMapper;
        this.redisUtil = redisUtil;
    }

    /**
     * 首页获取推荐分类
     * */
    @Override
    public List<CategoryVO> listCategoryVO() {
        List<CategoryRecommendsVO> categoryRecommendsVOList = categoryRecommendsMapper.queryCategoriesc();
        if (categoryRecommendsVOList.isEmpty()) {
            throw new MyException(ExceptionEnum.CATEGORY_RECOMMEDN_NOT_FOUND);
        }
        List<CategoryVO> categoryVOList = new ArrayList<>();

        for (CategoryRecommendsVO categoryRecommendsVO : categoryRecommendsVOList) {

            CategoryVO categoryVO = new CategoryVO();
            categoryVO.setCategoryRecommend(categoryRecommendsVO);
            categoryVO.setCategories(categoryMapper.category(categoryRecommendsVO.getCategoryId()));
            // 显示前 14个商品数据
            List<GoodsVO> goodsVOList = goodsSpuMapper.goods(categoryRecommendsVO.getCategoryId(), 14);

            listGoodsVO(goodsVOList);
            categoryVO.setProducts(goodsVOList);
            categoryVOList.add(categoryVO);
        }
        return categoryVOList;
    }

    /**
     * 获取热销商品
     * */
    @Override
    public List<GoodsVO> listBestSellingGoods() {
        // 前20条的热销商品
        LocalDateTime now = LocalDateTime.now();
        now = now.minusDays(30);
        List<CommoditySalesVO> commoditySalesVOList = orderDetailMapper.selectBestSellingGoods(now, 20);
        // 构建spuId集合
        List<String> spuIds = new ArrayList<>();
        for (CommoditySalesVO commoditySalesVO : commoditySalesVOList) {
            spuIds.add(commoditySalesVO.getSpuId());
        }
        // 获取前8条上架商品
        return goodsVOList(Collections.singleton(spuIds));
    }

    /**
     * 获取精品商品
     * */
    @Override
    public List<GoodsVO> listBoutiqueGoods() {
        // 前20条精品商品
        Set<Object> ids = redisUtil.zReverseRanges("collect", 0, 19);
        // 随机8条上架商品
        return  goodsVOList(ids);
    }

    @Override
    public Set<Object> listSearchKey() {

        return redisUtil.zReverseRanges("search", 0, 9);
    }

    /**
     * 根据分类查询商品
     * */
    @Override
    public PageResult<GoodsVO> querySpuByCategoryId(long cid, int pageNum, int rows) {
        //分页查询最多100条
        PageHelper.startPage(pageNum,Math.min(rows,100));
        Page<GoodsVO> pageInfo = (Page<GoodsVO>) this.goodsSpuMapper.pageListByCategoryId(cid);
        if (pageInfo.isEmpty()) {
            throw new MyException(ExceptionEnum.SPU_NOT_FOUND);
        }
        List<GoodsVO> list = pageInfo.getResult();
        listGoodsVO(list);
        return new PageResult<>(pageInfo.getTotal(), pageInfo.getPages(), pageInfo.getPageNum(), pageInfo.getPageSize(), list);
    }

    /**
     * 根据关键词查询商品
     * */
    @Override
    public PageResult<GoodsVO> searchGoodsSpu(String key, int pageNum, int rows) {
        // 用于搜索排序
        Double score = redisUtil.zScore("search", key);
        if (score == null) {
            redisUtil.zSet("search", key, 1);

        }else {
            redisUtil.zIncrBy("search", key, 1);
        }

        PageHelper.startPage(pageNum,Math.min(rows,100));
        Page<GoodsVO> pageInfo = (Page<GoodsVO>) this.goodsSpuMapper.pageListByKey(key);
        List<GoodsVO> list = pageInfo.getResult();
        listGoodsVO(list);
        return new PageResult<>(pageInfo.getTotal(), pageInfo.getPages(), pageInfo.getPageNum(), pageInfo.getPageSize(), list);
    }

    /**
     * 根据需要修改商品展示信息
     * */
    public void listGoodsVO(List<GoodsVO> list) {
        /*
            // 获取SpuId集合
            List<String> spuIds = list.stream().map(GoodsVO::getSpuId).collect(Collectors.toList());
            // 通过spuId集合查询spu的第一个sku商品
            List<GoodsSku> goodsSkuList = goodsSkuMapper.selectSkuBySpuIds(Collections.singleton(spuIds));
            // 配置spuId -> Sku的映射
            Map<String, GoodsSku> goodsSkuMap = new HashMap<>();
            for (GoodsSku goodsSku : goodsSkuList) {
                goodsSkuMap.put(goodsSku.getSpuId(), goodsSku);
            }
        */
        list.forEach(goodsVO -> {
            List<String> images = Arrays.asList(goodsVO.getImages().split(",").clone());
            goodsVO.setImageUrls(images);
        });
    }

    public List<GoodsVO> goodsVOList(Collection<Object> spuIds) {
        List<GoodsVO> goodsVOList = goodsSpuMapper.listGoodsBySpuIds(spuIds);
        /*
            List<GoodsSku> goodsSkuList = goodsSkuMapper.selectSkuBySpuIds(spuIds);
            // 构建skuId->Sku
            Map<String, GoodsSku> skuMap = new HashMap<>();
            for (GoodsSku goods : goodsSkuList) {
                skuMap.put(goods.getSpuId(), goods);
            }
        */
        goodsVOList.forEach(goodsVO -> {
            List<String> images = Arrays.asList(goodsVO.getImages().split(",").clone());
            goodsVO.setImageUrls(images);
        });
        return goodsVOList.subList(0, Math.min(8, goodsVOList.size()));
    }
}
