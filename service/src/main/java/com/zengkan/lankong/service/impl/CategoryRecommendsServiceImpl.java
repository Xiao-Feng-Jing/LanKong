package com.zengkan.lankong.service.impl;

import com.zengkan.lankong.enums.ExceptionEnum;
import com.zengkan.lankong.exception.MyException;
import com.zengkan.lankong.mappers.CategoryMapper;
import com.zengkan.lankong.mappers.CategoryRecommendsMapper;
import com.zengkan.lankong.pojo.CategoryRecommends;
import com.zengkan.lankong.service.CategoryRecommendsService;
import com.zengkan.lankong.vo.CategoryRecommendsVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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

    private final CategoryRecommendsMapper categoryRecommendsMapper;
    private final CategoryMapper categoryMapper;

    @Autowired
    public CategoryRecommendsServiceImpl(CategoryRecommendsMapper categoryRecommendsMapper, CategoryMapper categoryMapper) {
        this.categoryRecommendsMapper = categoryRecommendsMapper;
        this.categoryMapper = categoryMapper;
    }


    @Override
    public List<CategoryRecommendsVO> queryCategories() {
        List<CategoryRecommendsVO> categoryRecommendsVOList = categoryRecommendsMapper.queryCategoriesc();
        if (categoryRecommendsVOList.isEmpty()) {
            throw new MyException(ExceptionEnum.CATEGORY_RECOMMEDN_NOT_FOUND);
        }
        for (CategoryRecommendsVO categoryRecommendsVO : categoryRecommendsVOList) {
            categoryRecommendsVO.setCategoryName(categoryMapper.findByIdToName(categoryRecommendsVO.getId()));
        }
        return categoryRecommendsVOList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(long id) {
        categoryRecommendsMapper.deleteById(id);
    }

    /**
     * 添加推荐分类
     * @param categoryRecommends 分类id
     * @return 推荐分类对外值对象
     * */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public CategoryRecommendsVO saveCategory(CategoryRecommends categoryRecommends) {
        categoryRecommends.setCreateTime(LocalDateTime.now());
        categoryRecommends.setUpdateTime(categoryRecommends.getCreateTime());
        categoryRecommendsMapper.saveCategory(categoryRecommends);
        CategoryRecommendsVO categoryRecommendsVO = new CategoryRecommendsVO();
        // 属性拷贝
        BeanUtils.copyProperties(categoryRecommends, categoryRecommendsVO);
        // 获取分类名
        categoryRecommendsVO.setCategoryName(categoryMapper.findByIdToName(categoryRecommends.getCategoryId()));
        return categoryRecommendsVO;
    }

    /**
     * 更新推荐分类
     * @param categoryRecommendsVO 分类
     * */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCategory(CategoryRecommendsVO categoryRecommendsVO) {
        categoryRecommendsVO.setUpdateTime(LocalDateTime.now());
        categoryRecommendsMapper.updateCategory(categoryRecommendsVO);
    }
}
