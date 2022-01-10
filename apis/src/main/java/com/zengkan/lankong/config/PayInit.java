package com.zengkan.lankong.config;

import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.kernel.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : ZengKan
 * @version :
 * @Date : 2022/01/10/14:30
 * @Description :
 * @modified By :
 **/
@Component
public class PayInit implements ApplicationRunner {

    @Value("${alipay.appId}")
    private String appId;

    @Value("${alipay.privateKey}")
    private String privateKey;

    @Value("${alipay.publicKey}")
    private String publicKey;

    @Value("${alipay.gateway}")
    private String gateway;

    @Value("${alipay.notifyUrl}")
    private String notifyUrl;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        Factory.setOptions(getOptions());
    }

    private Config getOptions() {
        Config config = new Config();
        config.protocol = "https";
        config.gatewayHost = this.gateway;
        config.signType = "RSA2";
        config.appId = this.appId;
        config.merchantPrivateKey = this.privateKey;
        config.alipayPublicKey = this.publicKey;

        config.notifyUrl = this.notifyUrl;
        return config;
    }
}
