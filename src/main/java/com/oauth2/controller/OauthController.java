package com.oauth2.controller;

import com.oauth2.dto.OauthToken;
import kong.unirest.Unirest;
import org.apache.commons.codec.binary.Base64;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OauthController {
  // 클라이언트가 구현해야하는 코드 - 발급 받은 코드로 토큰 발행
  @GetMapping("/callback")
  public OauthToken.response code(@RequestParam String code){

    String cridentials = "clientId:secretKey";
    // base 64로 인코딩 (basic auth 의 경우 base64로 인코딩 하여 보내야한다.)
    String encodingCredentials = new String(
        Base64.encodeBase64(cridentials.getBytes())
    );
    OauthToken.request.accessToken request = new OauthToken.request.accessToken();
    request.setCode(code);
    request.setGrantType("authorization_code");
    request.setRedirectUri("http://localhost:8081/callback");
    // oauth 서버에 http 통신으로 토큰 발행
    return Unirest.post("http://localhost:8081/oauth/token")
        .header("Authorization","Basic "+encodingCredentials)
        .fields(request.getMapData())
        .asObject(OauthToken.response.class).getBody();
  }

}
