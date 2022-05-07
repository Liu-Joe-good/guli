package com.atguigu.gateway.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.util.pattern.PathPatternParser;

/**
 * <p>
 * 处理跨域
 * </p>
 *
 * @author qy
 * @since 2019-11-21
 */

/**
 * description:
 * 公共配置类
 * @author wangpeng
 * @date 2019/01/02
 */
@Configuration
public class CorsConfig {
    /**
     * 让所有请求都没有跨域,即之前其他controller都不用加@crossOrign注解了,(不然会出现跨域问题，即域名好了再跨回去了)
      */
    @Bean
    public CorsWebFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedMethod("*");
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(new PathPatternParser());
        source.registerCorsConfiguration("/**", config);

        return new CorsWebFilter(source);
    }
}

