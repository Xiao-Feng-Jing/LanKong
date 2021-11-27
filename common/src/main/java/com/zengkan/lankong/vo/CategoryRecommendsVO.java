package com.zengkan.lankong.vo;

import com.zengkan.lankong.pojo.CategoryRecommends;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : ZengKan
 * @version :
 * @Date : 2021/11/27/10:48
 * @Description : 推荐分类管理接口
 * @modified By :
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class CategoryRecommendsVO extends CategoryRecommends implements Serializable {
    private static final long serialVersionUID = -7985751122493041301L;

    /**
     * 推荐分类名
     * */
    private String categoryName;
}
