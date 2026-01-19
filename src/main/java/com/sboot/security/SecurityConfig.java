package com.sboot.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private JWTFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(request -> {
                CorsConfiguration config = new CorsConfiguration();
                config.setAllowedOrigins(List.of("http://localhost:4200")); // Angular frontend
                config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                config.setAllowedHeaders(List.of("*"));
                config.setAllowCredentials(true);
                return config;
            }))
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // ✅ Public endpoints (no authentication required)
                .requestMatchers(
                    "/auth/login",
                    "/setup/init",
                    "/testmail",
                    "/api/supplier-payments",
                    "/api/supplier-payments/**",
                    "/payments/**",
                    "/api/order/customer",
                    "/api/products/**",
                    "/api/suppliers",
                    "/api/purchase-order/**",
                    "/api/orders",
                    "/products/**",
                    "/payments/**",
                    "/api/orders/**",
                    "/api/orders/*/cancel",
                    "/api/internal-orders",
                    "/api/return-orders",
                    "/api/invoices/**",
                    "/api/supplier-invoices/**",
                    "/images/**",
                    "/api/users/search",
                    "/api/users/reset-password-with-otp",
                    "/api/users/forgot-password",
                    "/api/users/unlock-account",
                    "/api/production-schedules/**",
                    "/api/schedules/**",
                    "/api/units",
                    "/api/users/**",
                    "api/supplier-payments",
                    "api/supplier-payments/**",
                    "/api/production-timeline/**",
                    "/api/production-tracking",
                    "/api/products/stock-view",
                    "/api/track-production/**",

                    "/categories/**",

                    "/suppliers/**",
                    "/api/suppliers/performance",

                    "/api/reports/production/**",
                    "/api/productionefficiencyreports/**",
                    "/api/reports/trends/**",     //Added for report of production
                    "/api/reports/suppliers/**",



                    "/api/supplier-payments/**",
                    //Added for report of production
                    "/api/productionefficiencyreports/**",
                    "/api/suppliershistory/**",//Added for report of production
                    "/api/stats/**",
                    "/api/alerts/**",
                    "/api/orders/supplier",
                    
                    "/api/kpi/**"




                ).permitAll()

                // ✅ Allow public user registration
                .requestMatchers(HttpMethod.POST, "/api/users/register").permitAll()

                // ✅ Protected user profile endpoints
                .requestMatchers(HttpMethod.GET, "/api/users/*/profile").authenticated()
                .requestMatchers(HttpMethod.PUT, "/api/users/*/profile").authenticated()
                .requestMatchers(HttpMethod.POST, "/api/users/*/profile/image").authenticated()

                // ✅ Admin-only endpoints
                .requestMatchers("/api/users/unlock-account").hasAuthority("ADMIN")

                // ✅ All other requests require authentication
                .anyRequest().authenticated()
            )
            .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // ✅ Add JWT filter before UsernamePasswordAuthenticationFilter
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

// ✅ Web configuration for serving uploaded images
@Configuration
class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path uploadDir = Paths.get("uploads");
        String uploadPath = uploadDir.toFile().getAbsolutePath();

        // Map URL /images/** → physical folder uploads/
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:" + uploadPath + "/");

    
}
}    