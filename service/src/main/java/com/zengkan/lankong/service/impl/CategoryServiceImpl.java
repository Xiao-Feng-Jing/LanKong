package com.zengkan.lankong.service.impl;

import com.zengkan.lankong.mappers.CategoryMapper;
import com.zengkan.lankong.pojo.GoodsCategory;
import com.zengkan.lankong.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zengkan
 * @Date: 2021/02/25/17:19
 * @Description:
 **/
@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryMapper categoryMapper;

    @Autowired
    public CategoryServiceImpl(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    @Override
    public List<GoodsCategory> category(long pid) {
        return categoryMapper.category(pid);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(long cid) {
        /*
         * 先根据id查询要删除的对象，
         * 然后判断父节点孩子的个数，如果孩子不为0，则不做修改；如果孩子个数为0，则修改父节点isParent
         * 的值为false.
         * 再判断
         * 如果是父节点，那么删除所有附带子节点
         * 如果是子节点，那么只删除自己,
         */
        GoodsCategory category = categoryMapper.findById(cid);

        if (category.isParent()) {
            List<GoodsCategory> nodes = new CopyOnWriteArrayList<>();
            queryAllChildNodes(category, nodes);

            for (GoodsCategory node : nodes) {
                categoryMapper.deleteById(node.getCategoryId());
            }
        }else {
            categoryMapper.deleteById(category.getCategoryId());
        }
        List<GoodsCategory> list = categoryMapper.category(category.getParentId());
        if (list.size() == 1){
            categoryMapper.updateByIdAndIsParent(category.getParentId(), false);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public GoodsCategory saveCategory(GoodsCategory goodsCategory) {
        /*
        将本节点插入到数据库中,并返回id
        将此category的父节点的isParent设为true
        */
        categoryMapper.insert(goodsCategory);
        categoryMapper.updateByIdAndIsParent(goodsCategory.getParentId(), true);
        return goodsCategory;
    }

    /**
     * 更新节点
     * @param goodsCategory 新的节点数据
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateCategory(GoodsCategory goodsCategory) {
        /*更新本节点*/
        categoryMapper.updateByIdAndName(goodsCategory);
    }


    /**
     * 查询商品分类名
     * @param asList 分类id集合
     * @return 分类名集合
     * */
    @Override
    public List<String> queryNameByIds(List<Long> asList) {
        List<String> names = new CopyOnWriteArrayList<>();
        if (asList != null && !asList.isEmpty()){
            for (Long id : asList) {
                if (id !=null && id !=0){
                    names.add(categoryMapper
                            .findById(id)
                            .getCategoryName());
                }
            }
        }
        return names;
    }

    /**
     * 查询本节点下所有子节点
     * @param category 要查询的父节点
     * @param nodes 子节点的集合
     */
    private void queryAllChildNodes(GoodsCategory category, List<GoodsCategory> nodes) {
        nodes.add(category);

        List<GoodsCategory> list = categoryMapper.category(category.getCategoryId());

        for (GoodsCategory goodsCategory : list) {
            queryAllChildNodes(goodsCategory, nodes);
        }
    }

}
