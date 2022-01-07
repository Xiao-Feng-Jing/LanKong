package com.zengkan.lankong.service;

import com.zengkan.lankong.pojo.TbSpecification;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zengkan
 * @Date: 2021/03/02/18:57
 * @Description:
 **/
public interface SpecService {
    void save(TbSpecification tbSpecification);

    TbSpecification queryById(long id);

    void update(TbSpecification tbSpecification);

    void deleteByCategoryId(long cid);
}
