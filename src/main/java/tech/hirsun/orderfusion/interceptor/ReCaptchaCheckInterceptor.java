package tech.hirsun.orderfusion.interceptor;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.mysql.cj.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import tech.hirsun.orderfusion.result.ErrorMessage;
import tech.hirsun.orderfusion.result.Result;
import org.springframework.beans.factory.annotation.Value;

@Slf4j
@Component
public class ReCaptchaCheckInterceptor implements HandlerInterceptor {

    @Value("${recaptcha.secret}")
    private String serverToken;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // get the request url
        String requestURI = request.getRequestURI();
        log.info("Request URL: {}", requestURI);

        // get the recaptchaToken in the request header
        String recaptchaToken = request.getHeader("Recaptchatoken");

        // if the recaptchaToken is null, return false
        if(StringUtils.isNullOrEmpty(recaptchaToken)){
            log.info("The request header recaptchaToken is null");
            Result result = Result.error(ErrorMessage.RECAPTCHA_ERROR);

            String rcErrorJson = JSON.toJSONString(result);
            response.getWriter().write(rcErrorJson);

            return false;
        }

        //  verify the recaptchaToken
        boolean outcome = verifyReCaptchaToken(recaptchaToken);
        if(!outcome){
            log.info("The request header recaptchaToken is invalid");
            Result result = Result.error(ErrorMessage.RECAPTCHA_ERROR);

            String rcErrorJson = JSON.toJSONString(result);
            response.getWriter().write(rcErrorJson);

            return false;
        }else {
            // pass the interceptor
            log.info("The request header recaptchaToken is valid, pass the interceptor");
            return true;
        }
    }

    private boolean verifyReCaptchaToken(String clientToken){

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("secret", serverToken)
                .add("response", clientToken)
                .build();
        Request request = new Request.Builder()
                .url("https://www.recaptcha.net/recaptcha/api/siteverify")
                .post(body)
                .build();


        Call call = client.newCall(request);
        try (Response response = call.execute()) {
            String rspBody = response.body().string();
            JSONObject jsonObject= JSON.parseObject(rspBody);
            boolean outcome = jsonObject.getBoolean("success");
            log.info("The response from recaptcha.net: {}", jsonObject);
            return outcome;
        }catch (Exception e){
            log.error("Error in verifyReCaptchaToken in the http request with recaptcha.net.");
            return false;
        }


    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }
}
