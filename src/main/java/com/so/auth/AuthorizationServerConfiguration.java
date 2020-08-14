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
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import javax.sql.DataSource;

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
		
		endpoints.setClientDetailsService(clientDetails);
		endpoints.userDetailsService(userDetailsService);
//		endpoints.tokenServices(tokenServices());
		endpoints.tokenStore(tokenStore());
		endpoints.accessTokenConverter(jwtAccessTokenConverter);
//		endpoints.exceptionTranslator(new MyWebResponseExceptionTranslator());
//		endpoints.authenticationManager(authenticationManager)

	}
	
	@Override
	public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {//配置令牌端点(Token Endpoint)的安全约束
		oauthServer.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()");
		  oauthServer.allowFormAuthenticationForClients();
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		//配置客户端详情，与持久化信息联用
		clients.jdbc(dataSource);
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
