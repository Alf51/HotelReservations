package org.goldenalf.privatepr.config;

import org.goldenalf.privatepr.config.security.CustomConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
public class SecurityCustomConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(request -> request
                .requestMatchers("/books/all").hasRole("ADMIN")
                .requestMatchers("/books/**").hasAnyRole("ADMIN", "USER")
                .requestMatchers("/client/addRole/**").hasRole("ADMIN")
                .requestMatchers("/client/removeRole/**").hasRole("ADMIN")
                .requestMatchers("/client/allRoles/**").hasRole("ADMIN")
                .requestMatchers("/client/all").hasRole("ADMIN")
                .requestMatchers("/client/new").permitAll()
                .requestMatchers("/client/**").hasAnyRole("ADMIN", "USER")
                .requestMatchers(HttpMethod.PATCH, "/hotel/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/hotel/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/hotel/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/hotel/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/review/**").hasAnyRole("ADMIN", "USER")
                .requestMatchers(HttpMethod.PATCH, "/review/**").hasAnyRole("ADMIN", "USER")
                .requestMatchers(HttpMethod.DELETE, "/review/**").hasAnyRole("ADMIN", "USER")
                .requestMatchers(HttpMethod.GET, "/review/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/rooms/**").permitAll()
                .requestMatchers(HttpMethod.PATCH, "/rooms/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/rooms/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/rooms/**").hasRole("ADMIN")
                .requestMatchers("/auth/**").permitAll()
        );
        http.cors(configurer -> configurer.configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues()));
        http.csrf(AbstractHttpConfigurer::disable);
        http.apply(new CustomConfigurer<>());
        http.logout(configurer -> configurer.logoutSuccessUrl("/auth/success"));
        return http.build();
    }
}
