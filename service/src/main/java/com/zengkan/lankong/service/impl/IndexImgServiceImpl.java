package com.zengkan.lankong.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.zengkan.lankong.enums.ExceptionEnum;
import com.zengkan.lankong.exception.MyException;
import com.zengkan.lankong.mappers.IndexImgMapper;
import com.zengkan.lankong.pojo.IndexImg;
import com.zengkan.lankong.service.IndexImgService;
import com.zengkan.lankong.utils.RedisUtil;
import com.zengkan.lankong.utils.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zengkan
 * @Date: 2021/04/21/0:37
 * @Description:
 **/
@Service
@Slf4j
public class IndexImgServiceImpl implements IndexImgService {

    private static final String INDEX_IMG = "IndexImg";

    private final IndexImgMapper indexImgMapper;

    private final RedisUtil redisUtil;

    @Autowired
    public IndexImgServiceImpl(IndexImgMapper indexImgMapper, RedisUtil redisUtil) {
        this.indexImgMapper = indexImgMapper;
        this.redisUtil = redisUtil;
    }

    @Override
    public List<IndexImg> queryUrlList() {
        List<IndexImg> list = null;
        //首页轮播图是热点数据
        Object jsonData = redisUtil.getString(INDEX_IMG);
        if (jsonData != null) {
            list = JSON.parseArray(JSON.toJSONString(jsonData), IndexImg.class);
        }else {
            jsonData = redisUtil.getString(INDEX_IMG);
            if (jsonData == null) {
                String uuid = UUIDUtil.uuid();
                try {
                    if (Boolean.FALSE.equals(redisUtil.lock("lock",uuid))) {
                        return list;
                    }
                    jsonData = redisUtil.getString(INDEX_IMG);
                    if (jsonData == null) {
                        list = indexImgMapper.queryUrl();
                        redisUtil.setString(INDEX_IMG, list, 3600);
                    }
                } catch (Exception e) {
                    log.error("Redis加锁后， 处理过程异常");
                } finally {
                    redisUtil.unlock("lock", uuid);
                }
            }
            if (list == null) {
                list = JSON.parseArray(JSON.toJSONString(jsonData), IndexImg.class);
            }
        }

        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public IndexImg save(IndexImg indexImg) {
        String id = UUIDUtil.uuid();
        indexImg.setId(id);
        indexImg.setCreateTime(new Date());
        indexImg.setUpdateTime(indexImg.getCreateTime());
        int n = indexImgMapper.saveIndexImg(indexImg);
        if (n > 0) {
            return indexImg;
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public IndexImg update(IndexImg indexImg) {
        indexImg.setUpdateTime(new Date());
        int n = indexImgMapper.updateIndexImg(indexImg);
        if (n > 0) {
            return indexImg;
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteById(String id) {
        int n = indexImgMapper.deleteById(id);
        if (n <= 0) {
            throw new MyException(ExceptionEnum.CAROUSEL_NOT_FOUND);
        }
        return true;
    }
}
