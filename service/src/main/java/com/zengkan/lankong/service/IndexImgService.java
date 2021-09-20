package com.zengkan.lankong.service;


import com.zengkan.lankong.pojo.IndexImg;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zengkan
 * @Date: 2021/04/21/0:35
 * @Description:
 **/
public interface IndexImgService {

    List<IndexImg> queryUrlList();

    IndexImg save(IndexImg indexImg);

    IndexImg update(IndexImg indexImg);

    boolean deleteById(String id);
}
