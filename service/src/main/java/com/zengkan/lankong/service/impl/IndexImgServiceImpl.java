package com.zengkan.lankong.service.impl;

import com.zengkan.lankong.mappers.IndexImgMapper;
import com.zengkan.lankong.pojo.IndexImg;
import com.zengkan.lankong.service.IndexImgService;
import com.zengkan.lankong.utils.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
public class IndexImgServiceImpl implements IndexImgService {

    private final IndexImgMapper indexImgMapper;

    @Autowired
    public IndexImgServiceImpl(IndexImgMapper indexImgMapper) {
        this.indexImgMapper = indexImgMapper;
    }

    @Override
    public List<IndexImg> queryUrlList() {
        return indexImgMapper.queryUrl();
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
        return n > 0;
    }
}
