package com.so.controller;

import com.alibaba.druid.support.json.JSONUtils;
import com.so.utils.JWTHelper;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * jwt解析验证
 * @author fu
 */
@RestController
@RequestMapping(produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
public class JwtController {

    /**
     * 检测jwt方法
     * @return
     */
    @GetMapping(value = "/jwt/check")
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
    @GetMapping(value = "/jwt/test")
    public String jwtCheck2(HttpServletRequest httpServletRequest) {
        return "test";
    }
}
