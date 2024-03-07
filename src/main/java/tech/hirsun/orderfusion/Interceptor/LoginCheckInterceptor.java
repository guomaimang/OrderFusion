package tech.hirsun.orderfusion.Interceptor;

import com.alibaba.fastjson2.JSON;
import com.mysql.cj.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import tech.hirsun.orderfusion.result.CodeMessage;
import tech.hirsun.orderfusion.result.Result;
import tech.hirsun.orderfusion.utils.JwtUtils;

@Slf4j
@Component
public class LoginCheckInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // get the request url
        String requestURI = request.getRequestURI();
        log.info("Request URL: {}", requestURI);

        // get the jwt in the request header
        String jwt = request.getHeader("jwt");

        // if the jwt is null, return false
        if(!StringUtils.isNullOrEmpty(jwt)){
            log.info("The request header jwt is null, return not logged in information");
            Result result = Result.error(CodeMessage.USER_NOT_LOGIN);

            String notLoginJson = JSON.toJSONString(result);
            response.getWriter().write(notLoginJson);

            return false;
        }

        // parse the jwt, if the jwt is invalid, return false
        try {
            JwtUtils.parseJWT(jwt);
        }catch (Exception e){
            log.info("The request header jwt is invalid, return not logged in information");
            Result result = Result.error(CodeMessage.USER_NOT_LOGIN);

            String notLoginJson = JSON.toJSONString(result);
            response.getWriter().write(notLoginJson);

            return false;
        }

        // pass the interceptor
        log.info("The request header jwt is valid, pass the interceptor");
        return true;

    }
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }

}
