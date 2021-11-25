package com.zengkan.lankong.controller.index;

import com.zengkan.lankong.service.IndexService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : ZengKan
 * @version :
 * @Date : 2021/09/26/16:45
 * @Description : 首页门户
 * @modified By :
 **/
@RestController("portal/index")
@Api(value = "提供首页数据显示所需的接口",tags = "商城首页数据")
public class IndexController {

    private final IndexService indexService;

    public IndexController(IndexService indexService) {
        this.indexService = indexService;
    }
}
