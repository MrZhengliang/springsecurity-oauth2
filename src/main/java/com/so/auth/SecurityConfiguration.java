package com.so.auth;

import com.so.utils.MD5Encrypt;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

import javax.servlet.http.HttpServletRequest;

/**
 * @author fu
 */
@Slf4j
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private Logger logger = LoggerFactory.getLogger(SecurityConfiguration.class);

    private static final String NAME_AND_SALT ="123";

    @Bean
    public static NoOpPasswordEncoder passwordEncoder() {
        return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setHideUserNotFoundExceptions(false);
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Autowired
    public void globalUserDetails(AuthenticationManagerBuilder auth) throws Exception {
        //配置用户来源于数据库
        auth.userDetailsService(userDetailsService()).passwordEncoder(new org.springframework.security.crypto.password.PasswordEncoder() {
            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                // 无需验证密码
                if ("no_pass".equals(rawPassword)) {
                    return true;
                }
                if (encodedPassword != null) {
                    String passwordEn = encodedPassword.substring(0, 32);
//                    String nameAndsalt = encodedPassword.substring(32);
                    return MD5Encrypt.encrypt(rawPassword + NAME_AND_SALT).equals(passwordEn);
                }
                return false;
            }

            @Override
            public String encode(CharSequence rawPassword) {
                return rawPassword.toString();
            }
        });
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // 忽略所有管理监控地址
        web.ignoring().antMatchers("/actuator/**");
        web.ignoring().antMatchers("/logout");
        web.ignoring().antMatchers("/verify");
        web.ignoring().antMatchers("/jwt/check");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //basic 登录方式
        http.authorizeRequests().antMatchers(HttpMethod.OPTIONS).permitAll().anyRequest().authenticated()
                .and()
                .httpBasic().disable().csrf().disable();
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetailsService userDetailsService = new UserDetailsService(){
            @Override
            public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
                // 固定密码，后续这块可以改为自有项目的user信息模块
                String password = "123456";
                password = MD5Encrypt.encrypt(password + NAME_AND_SALT);
//                password = password + userName + NAME_AND_SALT;
                UserDetails userDetails = new User(userName, password, AuthorityUtils.createAuthorityList(new String[]{"ROLE_ANONYMOUS"}));

                return userDetails;
            }
        };

        return userDetailsService;
    }
}
