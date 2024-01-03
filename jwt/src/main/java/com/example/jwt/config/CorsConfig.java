package com.example.jwt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter(){
        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        corsConfiguration.setAllowCredentials(true); // 내 서버가 응답을 할 때 json을 Js에서 처리할 수 있게 할지 설정
        corsConfiguration.addAllowedOrigin("*"); // 모든 ip에 응답을 허용
        corsConfiguration.addAllowedHeader("*"); // 모든 header에 응답을 허용
        corsConfiguration.addAllowedMethod("*"); // 모든 restfulApi 요청을 허용
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/api/**",corsConfiguration);

        return new CorsFilter(urlBasedCorsConfigurationSource);
    }
}
