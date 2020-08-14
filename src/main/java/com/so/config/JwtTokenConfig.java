package com.so.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.FileSystemResource;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;


@Configuration
public class JwtTokenConfig {

    private Logger logger = LoggerFactory.getLogger(JwtTokenConfig.class);

    @Value("${jwt.sign.key}")
    private String jwtSignKey;
    @Value("${jwt.sign.jks.path}")
    private String jwtSignJksPath;
    @Value("${jwt.sign.type}")
    private String jwtSignType;
    @Value("${jwt.sign.jks.password}")
    private String jwtSignJksPassword;
    @Value("${jwt.sign.jks.alias}")
    private String jwtSignJksAlias;


    @Primary
    @Bean
    public JwtAccessTokenConverter accessTokenConverter() throws Exception {
        JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter() {
//            /***
//             * 重写增强token方法,用于自定义一些token返回的信息
//             */
            @Override
            public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
////				String userName = authentication.getUserAuthentication().getName();
//                CustomUser user = (CustomUser) authentication.getUserAuthentication().getPrincipal();
//                /** 自定义一些token属性 ***/
//                final Map<String, Object> additionalInformation = new HashMap<>();
////				additionalInformation.put("userName", userName);
//                additionalInformation.put("userId", user.getUserId());
//                additionalInformation.put("softCode", user.getSoftCode());
//                additionalInformation.put("userNo", user.getUserNo());
//                additionalInformation.put("userName", user.getUsername());
//                additionalInformation.put("realName", user.getRealName());
//                additionalInformation.put("code", AuthorizationErrorCodes.OK.code());
//                additionalInformation.put("message", "登录成功");
////				additionalInformation.put("roles", user.getAuthorities());
//                ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInformation);
                OAuth2AccessToken enhancedToken = super.enhance(accessToken, authentication);
                return enhancedToken;
            }
        };
        if ("1".equals(jwtSignType)) {//对称加密
            try {
                accessTokenConverter.setSigningKey(jwtSignKey);
            } catch (Exception e) {
                logger.error("获取token出错 -----------> {}", e);
                throw e;
            } finally {
            }

        } else if ("2".equals(jwtSignType)) {//非对称加密
            accessTokenConverter.setKeyPair(new KeyStoreKeyFactory(new FileSystemResource(jwtSignJksPath), jwtSignJksPassword.toCharArray()).getKeyPair(jwtSignJksAlias));
        }
        return accessTokenConverter;
    }

}
