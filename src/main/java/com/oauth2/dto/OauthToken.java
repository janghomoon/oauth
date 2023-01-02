package com.oauth2.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;

@Data
public class OauthToken {

  @JsonIgnoreProperties(ignoreUnknown = true)
  @Data
  public static class response{

    private String access_token;
    private String token_type;
    private String refresh_token;
    private long expires_in;
    private String scope;

  }

  @Data
  public static class request{

    @Data
    public static class accessToken {
      public String code;

      @JsonProperty("grant_type")
      private String grantType;

      @JsonProperty("redirect_uri")
      private String redirectUri;

      public HashMap<String, Object> getMapData() {
        HashMap<String, Object> map = new HashMap();
        map.put("code",code);
        map.put("grant_type",grantType);
        map.put("redirect_uri",redirectUri);
        return  map;
      }
    }

    @Data
    public static class refrashToken{
      private String refreshToken;
      private String grant_type;

      public Map getMapData(){
        Map map = new HashMap();
        map.put("refresh_token",refreshToken);
        map.put("grant_type",grant_type);
        return map;
      }
    }
  }
}
