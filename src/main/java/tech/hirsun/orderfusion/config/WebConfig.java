package tech.hirsun.orderfusion.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import tech.hirsun.orderfusion.interceptor.LoginCheckInterceptor;
import tech.hirsun.orderfusion.interceptor.ReCaptchaCheckInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private LoginCheckInterceptor loginCheckInterceptor;

    @Autowired
    private ReCaptchaCheckInterceptor reCaptchaCheckInterceptor;

    /**
     * Add the interceptor. The method will be called when the application starts.
     * @param registry is the InterceptorRegistry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // The larger the order, the later the interceptor is executed
        registry.addInterceptor(loginCheckInterceptor)
                .addPathPatterns("/demo/proc/**")
                .addPathPatterns("/userauth/refreshtoken")
                .addPathPatterns("/admin/**")
                .addPathPatterns("/order/**")
                .addPathPatterns("/seckill/**")
                .order(0);

        registry.addInterceptor(reCaptchaCheckInterceptor)
                .addPathPatterns("/userauth/login")
                .addPathPatterns("/order/general/create")
                .addPathPatterns("/order/seckill/create")
                .order(1);
    }
}
