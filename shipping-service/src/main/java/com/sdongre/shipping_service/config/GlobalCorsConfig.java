package com.sdongre.shipping_service.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
public class GlobalCorsConfig {
    @Value("${cors.allowed.origins}")
    private String[] allowedOrigins;

//    @Bean
//    CorsWebFilter corsWebFilter() {
//        CorsConfiguration corsConfig = new CorsConfiguration();
//        corsConfig.setAllowedOrigins(List.of("http://allowed-origin.com"));
//        corsConfig.setMaxAge(8000L);
//        corsConfig.addAllowedMethod("PUT");
//        corsConfig.addAllowedHeader("Baeldung-Allowed");
//
//        UrlBasedCorsConfigurationSource source =
//                new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", corsConfig);
//
//        return new CorsWebFilter(source);
//    }
@Bean
public CorsWebFilter corsWebFilter() {
    CorsConfiguration corsConfig = new CorsConfiguration();
    for (String origin : allowedOrigins) {
        corsConfig.addAllowedOrigin(origin);
    }
    corsConfig.addAllowedHeader("*");
    corsConfig.addAllowedMethod("*");
    corsConfig.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", corsConfig);
    return new CorsWebFilter(source);
}

}

