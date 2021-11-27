package com.zengkan.lankong.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zengkan
 * @Date: 2021/03/02/20:57
 * @Description:
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpuQueryByPage {
    /**
     *- page：当前页，int
     *- rows：每页大小，int
     *- key：搜索关键词，String
     *- saleable: 是否上下架
     *- isNew: 是否新品
     */
    private int page;
    private int rows;
    private String key;
    private String saleable;
    private String isNew;

}