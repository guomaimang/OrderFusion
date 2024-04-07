package tech.hirsun.orderfusion.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.hirsun.orderfusion.pojo.User;
import tech.hirsun.orderfusion.result.ErrorMessage;
import tech.hirsun.orderfusion.result.Result;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import tech.hirsun.orderfusion.service.UserService;
import tech.hirsun.orderfusion.utils.JwtUtils;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/userauth/oauth2")
public class Oauth2Controller {

    @Value("${azure.ad.client-id}")
    private String clientId;

    @Value("${azure.ad.client-secret}")
    private String clientSecret;

    @Value("${azure.ad.tenant-id}")
    private String tenantId;

    @Value("${azure.ad.redirect-uri}")
    private String redirectUri;

    @Autowired
    private UserService userService;

    @RequestMapping("/callback")
    public Result callback(@RequestBody String code) {
        code = JSON.parseObject(code).getString("code");

        OkHttpClient client = new OkHttpClient();
        okhttp3.RequestBody body = new FormBody.Builder()
                .add("grant_type", "authorization_code")
                .add("client_id", clientId)
                .add("code", code)
                .add("client_secret", clientSecret)
                .add("redirect_uri", redirectUri)
                .build();

        Request request = new Request.Builder()
                .url("https://login.microsoftonline.com/" + tenantId + "/oauth2/v2.0/token")
                .post(body)
                .build();

        Call call = client.newCall(request);
        try (Response response = call.execute()) {
            String responseBody = response.body().string();
            log.info("Response from Azure AD: {}", responseBody);
            String idToken = JSON.parseObject(responseBody).getString("id_token");

            SignedJWT ssoJwt = SignedJWT.parse(idToken);
            JWTClaimsSet claimsSet = ssoJwt.getJWTClaimsSet();

            String email = claimsSet.getClaim("email").toString();
            String displayName = claimsSet.getClaim("name").toString();

            log.info("SSO logged in, email: {}, displayName: {}", email, displayName);

            User u = userService.ssoLogin(email, displayName);

            Map<String, Object> claims = new HashMap<>();
            claims.put("id", u.getId());
            claims.put("email", u.getEmail());
            String jwt = JwtUtils.createJwt(claims);
            Map<String, Object> map = new HashMap<>();
            map.put("jwt", jwt);
            map.put("user", u);
            return Result.success(map);
        }catch (Exception e){
            e.printStackTrace();
            log.error("Error in verify Token in the http request with Azure AD");
            return Result.error(ErrorMessage.SERVER_ERROR);
        }
    }

}


