package com.zengkan.lankong.config;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zengkan
 * @Date: 2021/03/17/21:56
 * @Description: oss基础配置
 **/
@Configuration
@Data
public class OssConfig implements Serializable {

    private static final long serialVersionUID = 5423198405970463093L;//序列化id

    @Value("${aliyun.oss.urlPrefix}")
    private String ALIYUN_OSS_URLPREFIX;//浏览器访问地址
    @Value("${aliyun.oss.endpoint}")
    private String ALIYUN_OSS_ENDPOINT;//Bucket所在地域对应的Endpoint
    @Value("${aliyun.oss.accessKeyId}")
    private String ALIYUN_OSS_ACCESSKEYID;//访问权限id
    @Value("${aliyun.oss.accessKeySecret}")
    private String ALIYUN_OSS_ACCESSKEYSECRET;//访问权限秘钥
    @Value("${aliyun.oss.policy.expire}")
    private int ALIYUN_OSS_EXPIRE;//签名有效期(S)
    @Value(("${aliyun.oss.policy.maxSize}"))
    private int ALIYUN_OSS_MAX_SIZE;//上传文件大小(M)
    @Value("${aliyun.oss.policy.callback}")
    private String ALIYUN_OSS_CALLBACK;//文件上传成功后的回调地址
    @Value("${aliyun.oss.bucketName}")
    private String ALIYUN_OSS_BUCKET_NAME;//Bucket名
    @Value("${aliyun.oss.policy.dir.one}")
    private String ALIYUN_OSS_DIR_PREFIX_ONE;//图片上传文件夹路径前缀
    @Value("${aliyun.oss.policy.dir.two}")
    private String ALIYUN_OSS_DIR_PREFIX_TWO;//普通文件上传地址

    @Bean
    public OSS ossClient() {
        return new OSSClientBuilder().build(ALIYUN_OSS_ENDPOINT,ALIYUN_OSS_ACCESSKEYID,ALIYUN_OSS_ACCESSKEYSECRET);
    }
}
