package com.so.controller;

import com.alibaba.druid.support.json.JSONUtils;
import com.so.utils.JWTHelper;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 授权码模式验证
 * @author fu
 */
@RestController
@RequestMapping(produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
public class AuthorizeCodeController {

    /**
     * 检测jwt方法
     * @return
     */
    @GetMapping(value = "/authorize/check")
    public String jwtCheck(HttpServletRequest httpServletRequest) {
        String authorization = httpServletRequest.getHeader("Authorization");
        String token = StringUtils.substringAfterLast(authorization, " ");

        try {
            if (StringUtils.isNotBlank(token)) {
                Claims claims = JWTHelper.parseJWT(token, "123456");
                String ret = JSONUtils.toJSONString(claims);
                return ret;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 测试jwt/test是否有权限访问
     * @return
     */
    @GetMapping(value = "/authorize/test")
    public String jwtCheck2(HttpServletRequest httpServletRequest) {
        return "test";
    }
}
