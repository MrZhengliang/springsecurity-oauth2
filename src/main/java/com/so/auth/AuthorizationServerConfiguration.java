package com.so.auth;

import com.so.config.JwtTokenEnhancer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.approval.ApprovalStoreUserApprovalHandler;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import javax.sql.DataSource;

/**
 * 授权服务
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

	@Autowired
	private DataSource dataSource;

	@Autowired
	private JwtAccessTokenConverter jwtAccessTokenConverter;
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private ClientDetailsService clientDetails;
	
	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		//配置授权(authorization)以及令牌(token)的访问端点和令牌服务(token services)
		endpoints.authenticationManager(this.authenticationManager);
		endpoints.userApprovalHandler(new ApprovalStoreUserApprovalHandler());
		endpoints.setClientDetailsService(clientDetails);
		endpoints.userDetailsService(userDetailsService);
		endpoints.tokenStore(tokenStore());
		endpoints.accessTokenConverter(jwtAccessTokenConverter);
		// 授权码模式
//		endpoints.authorizationCodeServices(new JdbcAuthorizationCodeServices(dataSource));
//		endpoints.exceptionTranslator(new MyWebResponseExceptionTranslator());
//		endpoints.authenticationManager(authenticationManager)

	}
	
	@Override
	public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
		//配置令牌端点(Token Endpoint)的安全约束
		oauthServer.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()");
		//允许表单提交
		oauthServer.allowFormAuthenticationForClients();
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		//配置客户端详情，与持久化信息联用
		clients.jdbc(dataSource);

		/**
		 * .withClient("client1")//用于标识用户ID
		 * .authorizedGrantTypes("authorization_code","refresh_token")//授权方式
		 * .scopes("test")//授权范围
		 * .secret(PasswordEncoderFactories.createDelegatingPasswordEncoder().encode("123456"));//客户端安全码,secret密码配置从 Spring Security 5.0开始必须以 {bcrypt}+加密后的密码 这种格式填写;
		 */
	}

	/**
	 * redis token 配置
	 */
	//@Bean
//	public TokenStore redisTokenStore() {
//		return new RedisTokenStore(redisConnectionFactory);
//	}

	@Bean // 声明 ClientDetails实现
	public ClientDetailsService clientDetails() {
		return new JdbcClientDetailsService(dataSource);
	}

	@Bean
	public TokenStore tokenStore() {
//	    return new InMemoryTokenStore();
		return new JdbcTokenStore(dataSource);
	}

	@Bean
    public TokenEnhancer jwtTokenEnhancer(){
        return new JwtTokenEnhancer();
    }
}
