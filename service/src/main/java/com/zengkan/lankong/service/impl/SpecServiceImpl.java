package com.zengkan.lankong.service.impl;

import com.zengkan.lankong.mappers.SpecMapper;
import com.zengkan.lankong.pojo.TbSpecification;
import com.zengkan.lankong.service.SpecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zengkan
 * @Date: 2021/03/02/18:58
 * @Description:
 **/
@Service
public class SpecServiceImpl implements SpecService {

    private final SpecMapper specMapper;

    @Autowired
    public SpecServiceImpl(SpecMapper specMapper) {
        this.specMapper = specMapper;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void save(TbSpecification tbSpecification) {
        /*
        并将本模板插入到数据库中，
        */
        specMapper.save(tbSpecification);
    }

    /**
     * 查询分类下的规格模板
     * @param id 分类id
     * @return TbSpecification 模板
     * */
    @Override
    public TbSpecification queryById(long id) {
        return specMapper.queryById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(TbSpecification tbSpecification) {
        /*
        修改该分类下的模板
        */
        specMapper.updateById(tbSpecification);
    }

    @Override
    public void delete(long cid) {
        /*
        删除该分类id下的模板
        */
        specMapper.deleteById(cid);
    }
}
