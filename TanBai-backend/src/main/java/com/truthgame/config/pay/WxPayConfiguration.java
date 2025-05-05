package com.truthgame.config.pay;

import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Binary Wang
 */
@Configuration
@ConditionalOnClass(WxPayService.class)
@EnableConfigurationProperties(WxPayProperties.class)
@AllArgsConstructor
public class WxPayConfiguration {
  private WxPayProperties properties;

  @Bean
  @ConditionalOnMissingBean
  public WxPayService wxService() {
    WxPayConfig payConfig = new WxPayConfig();
    payConfig.setAppId(StringUtils.trimToNull(this.properties.getAppId()));//【V3商户模式需要】
    payConfig.setMchId(StringUtils.trimToNull(this.properties.getMchId()));//【V3商户模式需要】
    payConfig.setApiV3Key(StringUtils.trimToNull(this.properties.getApiV3Key()));//【V3商户模式需要】
    payConfig.setCertSerialNo(StringUtils.trimToNull(this.properties.getCertSerialNo()));//【V3商户模式需要】
//    payConfig.setPrivateCertPath(StringUtils.trimToNull(this.properties.getPrivateCertPath()));//【V3商户模式需要】 - 商户API证书（公钥验签不能配置！4.7.4.b版本SDK会触发autoUpdateCert()导致报错！）
    payConfig.setPrivateKeyPath(StringUtils.trimToNull(this.properties.getPrivateKeyPath()));//【V3商户模式需要】
    payConfig.setPublicKeyPath(StringUtils.trimToNull(this.properties.getPublicKeyPath()));//【V3商户模式需要】
    payConfig.setPublicKeyId(StringUtils.trimToNull(this.properties.getPublicKeyId()));//【V3商户模式需要】
    payConfig.setNotifyUrl(StringUtils.trimToNull(this.properties.getNotifyUrl()));//支付成功回调地址

    // 可以指定是否使用沙箱环境
    payConfig.setUseSandboxEnv(false);

    WxPayService wxPayService = new WxPayServiceImpl();
    wxPayService.setConfig(payConfig);
    return wxPayService;
  }

}
