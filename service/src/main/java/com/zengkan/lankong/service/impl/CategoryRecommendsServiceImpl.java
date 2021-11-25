package com.zengkan.lankong.service.impl;

import com.zengkan.lankong.service.CategoryRecommendsService;
import com.zengkan.lankong.vo.CategoryVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : ZengKan
 * @version :
 * @Date : 2021/09/26/16:37
 * @Description : 推荐分类的实现类
 * @modified By :
 **/
@Service
public class CategoryRecommendsServiceImpl implements CategoryRecommendsService {
    @Override
    public List<CategoryVO> queryCategories() {
        return null;
    }

    @Override
    public void deleteById(long id) {
        // TODO document why this method is empty
    }

    @Override
    public long saveCategory(long cid) {
        return 0;
    }

    @Override
    public void updateCategory(long id, long cid) {
        // TODO document why this method is empty
    }
}
