package com.zengkan.lankong.vo;

import com.zengkan.lankong.pojo.IndexImg;

import java.io.Serializable;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : ZengKan
 * @version :
 * @Date : 2021/09/26/17:06
 * @Description : 首页分区数据
 * @modified By :
 **/
public class HomeMetaVO implements Serializable {
    private static final long serialVersionUID = -3301537786477650635L;

    List<IndexImg> homeBannerList;

    List<FloorVO> homeFloorList;
}
