package com.zengkan.lankong.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zengkan.lankong.enums.ExceptionEnum;
import com.zengkan.lankong.exception.MyException;
import com.zengkan.lankong.mappers.GoodsSkuMapper;
import com.zengkan.lankong.mappers.GoodsSpuMapper;
import com.zengkan.lankong.mappers.SpuDetailMapper;
import com.zengkan.lankong.mappers.WarehouseGoodsMapper;
import com.zengkan.lankong.pojo.GoodsSku;
import com.zengkan.lankong.pojo.GoodsSpu;
import com.zengkan.lankong.pojo.SpuDetail;
import com.zengkan.lankong.pojo.WarehouseGoods;
import com.zengkan.lankong.service.CategoryService;
import com.zengkan.lankong.service.FileUploadService;
import com.zengkan.lankong.service.GoodsService;
import com.zengkan.lankong.utils.MultipartFileUtil;
import com.zengkan.lankong.utils.UUIDUtil;
import com.zengkan.lankong.vo.FileUploadResult;
import com.zengkan.lankong.vo.PageResult;
import com.zengkan.lankong.vo.SpuQueryByPage;
import com.zengkan.lankong.vo.SpuVo;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zengkan
 * @Date: 2021/03/02/20:17
 * @Description: 商品服务接口实现类
 **/
@Service
public class GoodsServiceImpl implements GoodsService {

    private static final Logger logger = LoggerFactory.getLogger(GoodsServiceImpl.class);

    private final GoodsSpuMapper goodsSpuMapper;
    private final CategoryService categoryService;
    private final GoodsSkuMapper goodsSkuMapper;
    private final SpuDetailMapper spuDetailMapper;
    private final WarehouseGoodsMapper warehouseGoodsMapper;
    private final FileUploadService fileUploadService;

    @Autowired
    public GoodsServiceImpl(GoodsSpuMapper goodsSpuMapper, CategoryService categoryService, GoodsSkuMapper goodsSkuMapper, SpuDetailMapper spuDetailMapper, WarehouseGoodsMapper warehouseGoodsMapper, FileUploadService fileUploadService) {
        this.goodsSpuMapper = goodsSpuMapper;
        this.categoryService = categoryService;
        this.goodsSkuMapper = goodsSkuMapper;
        this.spuDetailMapper = spuDetailMapper;
        this.warehouseGoodsMapper = warehouseGoodsMapper;
        this.fileUploadService = fileUploadService;
    }

    @Override
    public PageResult<SpuVo> pageList(SpuQueryByPage spuQueryByPage) {

        //分页查询最多100条
        PageHelper.startPage(spuQueryByPage.getPage(),Math.min(spuQueryByPage.getRows(),100));

        Page<GoodsSpu> pageInfo = (Page<GoodsSpu>) this.goodsSpuMapper.pageList(spuQueryByPage);

        List<SpuVo> list = pageInfo.getResult().stream().map(spu -> {
            SpuVo spuVo = new SpuVo();
            //1.属性拷贝
            BeanUtils.copyProperties(spu,spuVo);

            //2.查询spu的商品分类名称，各级分类
            List<String> nameList = this.categoryService.queryNameByIds(Arrays.asList(spu.getCid1(),spu.getCid2(),spu.getCid3()));
            //3.拼接名字,并存入
            spuVo.setCname(StringUtils.join(nameList,"/"));

            return spuVo;
        }).collect(Collectors.toList());

        return new PageResult<>(pageInfo.getTotal(), pageInfo.getPages(), pageInfo.getPageNum(), pageInfo.getPageSize(), list);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveGoods(SpuVo spuVo) {

        //保存spu
        //生成唯一spuId
        spuVo.setSpuId(UUIDUtil.uuid());
        /*默认上架*/
        spuVo.setSaleable(true);
        /*默认新品*/
        spuVo.setNew(true);
        spuVo.setCreateDate(new Date());
        spuVo.setModifiedTime(spuVo.getCreateDate());
        this.goodsSpuMapper.save(spuVo);

        //保存spuDetail
        SpuDetail spuDetail = spuVo.getSpuDetail();
        spuDetail.setSpuId(spuVo.getSpuId());
        detailsStringToMultipartFile(spuDetail,null,false);
        this.spuDetailMapper.save(spuDetail);

        //保存sku和库存信息
        saveSkuAndStock(spuVo.getSkus(),spuVo.getSpuId());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateGoods(SpuVo spuVo) {
        /*
         * 更新策略：
         *      1.判断spu_detail中的spec_template字段新旧是否一致
         *      2.如果一致说明修改的只是库存、价格和是否启用，那么就使用update
         *      3.如果不一致，说明修改了特有属性，那么需要把原来的sku全部删除，然后添加新的sku
         */
        //更新spu
        spuVo.setSaleable(true);
        spuVo.setModifiedTime(new Date());
        this.goodsSpuMapper.updateBySpuId(spuVo);
        //更新spuDetail
        SpuDetail spuDetail = spuVo.getSpuDetail();
        //获取old特有模板
        SpuDetail oldDetail = this.spuDetailMapper.queryById(spuVo.getSpuId());
        String oldTemplate = oldDetail.getSpecTemplate();
        //判断是否更改
        //更改 sku insert 未更改 sku insert
        updateSkuAndStock(spuVo.getSkus(),spuVo.getSpuId(), !spuDetail.getSpecTemplate().equals(oldTemplate));
        spuDetail.setSpuId(spuVo.getSpuId());
        detailsStringToMultipartFile(spuDetail,oldDetail,true);
        this.spuDetailMapper.updateBySpuId(spuDetail);
    }

    private void updateSkuAndStock(List<GoodsSku> skus, String spuId, boolean b) {
        //通过b判断是insert还是update true 更新，false 新增
        //获取当前数据库中spu_id = id的sku信息
        List<GoodsSku> oldSkuList = goodsSkuMapper.selectSkuBySpuId(spuId);
        if (b){
            updateSkuAndStock(oldSkuList,skus,spuId);
        }else {
            List<String> ids = oldSkuList.stream()
                    .map(GoodsSku::getSkuId)
                    .collect(Collectors.toList());
            //删除sku
            goodsSkuMapper.deleteBySpuId(Collections.singletonList(spuId));
            //删除库存
            if (!ids.isEmpty()) {
                warehouseGoodsMapper.deleteBySkuIds(ids);
            }

            //新增sku和库存
            saveSkuAndStock(skus,spuId);
        }
    }
    private void updateSkuAndStock(List<GoodsSku> oldSkuList,List<GoodsSku> skus,String spuId){
        //判断是更新时是否有新的sku被添加：
        //如果对已有数据更新的话，则此时oldList中的数据和skus中的ownSpec是相同的，
        //否则则需要新增
        int count = 0;
        for (GoodsSku sku : skus) {
            if (!sku.isEnable()){
                continue;
            }
            for (GoodsSku old : oldSkuList) {
                if (old.getOwnSpec().equals(sku.getOwnSpec())){
                    //更新sku
                    sku.setSkuId(old.getSkuId());
                    sku.setSpuId(old.getSpuId());
                    sku.setImages(old.getImages());
                    sku.setEnable(old.isEnable());
                    sku.setLastUpdateTime(new Date());
                    sku.setCreateTime(old.getCreateTime());
                    goodsSkuMapper.updateBySkuId(sku);
                    //更新库存信息
                    updateWarehouseGoods(sku);
                    oldSkuList.remove(old);
                    break;
                }else {
                    ++count;
                }
            }
            if (count == oldSkuList.size() && count !=0){
                //当只有一个sku时，更新完因为从oldList中将其移除，所以长度变为0，所以要需要加不为0的条件
                List<GoodsSku> addSkus = new CopyOnWriteArrayList<>();
                addSkus.add(sku);
                saveSkuAndStock(addSkus,spuId);
            }
            count = 0;
        }

        //处理脏数据
        if (!oldSkuList.isEmpty()){
            for (GoodsSku goodsSku : oldSkuList) {
                goodsSkuMapper.deleteBySkuId(goodsSku.getSkuId());
                warehouseGoodsMapper.deleteBySkuId(goodsSku.getSkuId());
            }
        }
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void goodsSoldOut(String spuId) {
        GoodsSpu goodsSpu = goodsSpuMapper.selectBySpuId(spuId);
        //上下架
        goodsSpu.setSaleable(!goodsSpu.isSaleable());
        goodsSpuMapper.updateBySpuIdSelective(goodsSpu);
    }

    /**
     * 商品删除二合一（多个单个）
     * @param ids id集合
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteGoods(List<String> ids) {
        //删除spu
        goodsSpuMapper.deleteGoods(ids);

        //删除spu_detail中的数据
        spuDetailMapper.deleteBySpuIds(ids);

        //删除sku
        List<GoodsSku> goodsSkuList = goodsSkuMapper.selectSkuBySpuIds(ids);
        List<String> skuIds = goodsSkuList.stream().map(GoodsSku::getSkuId).collect(Collectors.toList());
        goodsSkuMapper.deleteBySpuId(ids);
        //删除库存
        if (!skuIds.isEmpty()){
            warehouseGoodsMapper.deleteBySkuIds(skuIds);
        }

    }

    /**
     * 根据skuId查询sku
     * @param skuId String类型的主键
     * @return GoodsSku
     */
    @Override
    public GoodsSku querySkuBySkuId(String skuId) {
        GoodsSku goodsSku = goodsSkuMapper.querySkuBySkuId(skuId);
        if (goodsSku == null) {
            throw new MyException(ExceptionEnum.SKU_NOT_FOUND);
        }
        WarehouseGoods warehouseGoods = warehouseGoodsMapper.queryBySkuId(skuId);
        goodsSku.setStock(warehouseGoods.getCurrentCnt());
        return goodsSku;
    }

    /**
     * 根据spuId 查询 商品数据
     * */
    @Override
    public SpuVo querySpuById(String spuId) {
        /*
         * 第一页所需信息如下：
         * 1.商品的分类信息、所属品牌、商品标题、商品卖点（子标题）
         * 2.商品的包装清单、售后服务
         */
        GoodsSpu goodsSpu = goodsSpuMapper.selectBySpuId(spuId);
        if (goodsSpu == null) {
            throw new MyException(ExceptionEnum.SPU_NOT_FOUND);
        }
        //
        SpuDetail spuDetail = querySpuDetailBySpuId(spuId);

        List<GoodsSku> skus = querySkuBySpuId(spuId);
        SpuVo spuVo = new SpuVo();
        BeanUtils.copyProperties(goodsSpu,spuVo);
        spuVo.setSkus(skus);
        spuVo.setSpuDetail(spuDetail);
        return spuVo;
    }

    /**
     * 根据spu id查询所有的sku
     * @param spuId spuId
     * @return sku集合
     * */
    @Override
    public List<GoodsSku> querySkuBySpuId(String spuId) {
        List<GoodsSku> skuList = goodsSkuMapper.selectSkuBySpuId(spuId);

        if (skuList.isEmpty()) {
            throw new MyException(ExceptionEnum.SKU_NOT_FOUND);
        }
        List<String> ids = skuList.stream().map(GoodsSku::getSkuId).collect(Collectors.toList());
        List<WarehouseGoods> stockList = warehouseGoodsMapper.queryBySpuIds(ids);
        // 查询库存
        for (GoodsSku sku : skuList) {
            for (WarehouseGoods warehouseGoods : stockList) {
                if (warehouseGoods.getSkuId().equals(sku.getSkuId())){
                    sku.setStock(warehouseGoods.getCurrentCnt());
                    break;
                }
            }
        }
        return skuList;
    }

    /**
     * 根据spu id查询商品详情
     * @param spuId spuId
     * @return 商品详情
     * */
    @Override
    public SpuDetail querySpuDetailBySpuId(String spuId) {
        SpuDetail spu = spuDetailMapper.queryById(spuId);
        if (spu == null) {
            throw new MyException(ExceptionEnum.SPU_DETAIL_NOT_FOUND);
        }
        return spuDetailMapper.queryById(spuId);
    }

    /**
     * 保存商品信息
     * */
    private void saveSkuAndStock(List<GoodsSku> skus, String spuId) {
        for (GoodsSku sku : skus) {
            if (!sku.isEnable()){
                continue;
            }
            //保存sku
            sku.setSpuId(spuId);
            sku.setSkuId(UUIDUtil.uuid());
            sku.setCreateTime(new Date());
            sku.setLastUpdateTime(sku.getCreateTime());
            goodsSkuMapper.save(sku);
            saveWarehouseGoods(sku);

        }
    }


    /**
     * 保存商品库存
     * */
    private void saveWarehouseGoods(GoodsSku sku){
        WarehouseGoods warehouseGoods = new WarehouseGoods();
        warehouseGoods.setSkuId(sku.getSkuId());
        warehouseGoods.setCurrentCnt(sku.getStock());
        warehouseGoodsMapper.save(warehouseGoods);
    }


    private void updateWarehouseGoods(GoodsSku sku) {
        WarehouseGoods warehouseGoods = new WarehouseGoods();
        warehouseGoods.setSkuId(sku.getSkuId());
        warehouseGoods.setCurrentCnt(sku.getStock());
        warehouseGoodsMapper.updateBySkuId(warehouseGoods);
    }

    private void detailsStringToMultipartFile(SpuDetail spuDetail,SpuDetail oldSpuDetail,boolean isUpdate){
        FileUploadResult results = null;
        MultipartFileUtil multipartUtil = new MultipartFileUtil();
        multipartUtil.setFile(spuDetail.getDescription());
        multipartUtil.setBytes(spuDetail.getDescription().getBytes());
        multipartUtil.setFileName(String.valueOf(System.currentTimeMillis()));
        Map<String, Object> map = new ConcurrentHashMap<>();
        if (!isUpdate){
            map.put("minSize",0);
            results = fileUploadService.append(multipartUtil,null);
        }else {
            map = JSON.parseObject(oldSpuDetail.getDescription(),new TypeReference<Map<String,Object>>(){});

            results = fileUploadService.append(multipartUtil,(String) map.get("fileName"));
            map.put("minSize",Integer.parseInt(map.get("maxSize").toString())+1);
        }
        assert results != null;
        map.put("fileName",results.getName());
        map.put("maxSize",results.getFileSize()-1);
        spuDetail.setDescription(JSON.toJSONString(map));
    }
}
