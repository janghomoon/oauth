package com.oauth2.configration;

import com.oauth2.service.UserDetailService;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;

import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

@Configuration
@RequiredArgsConstructor
@EnableAuthorizationServer
public class Oauth2AuthorizationConfig extends AuthorizationServerConfigurerAdapter {
//  @Autowired
//  @Autowired
  private final UserDetailService userDetailService;

//  @Autowired
  private final AuthenticationManager authenticationManager;

  private final DataSource dataSource;

  @Bean
  public TokenStore tokenStore(){
    return new JdbcTokenStore(dataSource);
  }

  @Override
  public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
    security.tokenKeyAccess("permitAll()") // 모두 허용하지 않으면 해당 서버에서 토큰 접근이 불가능 하여 토큰을 DB에서 찾을 수 없다.
        .checkTokenAccess("isAuthenticated()") // 인증된 사용자만 토큰 체크 가능
        .allowFormAuthenticationForClients();
  }

//  @Override
//  public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
//    super.configure(security);
//  }

  // client 설정
  @Override
  public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
    clients.inMemory() // 클라이언트 정보는 메모리를 이용 한다.
        .withClient("clientId") // 클라이언트 아이디
        .secret("{noop}secretKey") // 시크릿키 ({} 안에 암호화 알고리즘을 명시 하면 된다. 암호화가 되어 있지 않다면 {noop}로 설정 해야 한다. 실제 요청은 암호화 방식인 {noop}를 입력 하지 않아도 된다.)
        .authorizedGrantTypes("authorization_code","password", "refresh_token", "client_credentials") // 가능한 토큰 발행 타입
        .scopes("read", "write") // 가능한 접근 범위
        .accessTokenValiditySeconds(60) // 토큰 유효 시간 : 1분
        .refreshTokenValiditySeconds(60*60) // 토큰 유효 시간 : 1시간
        .redirectUris("http://localhost:8081/callback") // 가능한 redirect uri
        .autoApprove(true); // 권한 동의는 자동으로 yes (false 로 할시 권한 동의 여부를 묻는다.)
  }

  // 인증, 토큰 설정
  @Override
  public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
    endpoints.authenticationManager(authenticationManager) // grant_type password를 사용하기 위함 (manager 지정 안할시 password type 으로 토큰 발행시 Unsupported grant type: password 오류 발생)
        .userDetailsService(userDetailService) // refrash token 발행시 유저 정보 검사 하는데 사용하는 서비스 설정
        .tokenStore(tokenStore());
  }
}
