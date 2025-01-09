package com.siukatech.poc.spring.security.demo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        log.info("filterChain - start");

        http.csrf(configurer -> configurer.disable());

        http.authorizeHttpRequests(requests -> requests
                .requestMatchers(AntPathRequestMatcher.antMatcher("/general/**"))
                .permitAll()
                //
                .requestMatchers(AntPathRequestMatcher.antMatcher("/user/**"))
//                .hasAnyRole("USER_01", "USER_02")
                .hasAnyAuthority("USER_01", "USER_02")
                //
                .requestMatchers(AntPathRequestMatcher.antMatcher("/admin/**"))
//                .hasAnyRole("ADMIN_01", "ADMIN_02")
                .hasAnyAuthority("ADMIN_01", "ADMIN_02")
                //
//                .anyRequest()
//                .authenticated()
        );

        // or
//        http.authorizeHttpRequests(requests -> requests
//                .requestMatchers(AntPathRequestMatcher.antMatcher("/general/**"))
//                .permitAll()
//        );
//        http.authorizeHttpRequests(requests -> requests
//                .requestMatchers(AntPathRequestMatcher.antMatcher("/user/**"))
//                .hasAnyRole("USER_01", "USER_02")
//        );
//        http.authorizeHttpRequests(requests -> requests
//                .requestMatchers(AntPathRequestMatcher.antMatcher("/admin/**"))
//                .hasAnyRole("ADMIN_01", "ADMIN_02")
//        );

        http.httpBasic(Customizer.withDefaults());

        http.sessionManagement(configurer -> configurer.disable());

        log.info("filterChain - end");

        return http.build();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails user = User.withUsername("user1")
                .password(passwordEncoder.encode("user1"))
                .roles("USER_01")
                .build();
        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
