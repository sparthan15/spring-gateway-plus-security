package com.masgobalconsulting.careerpath.apigateway.apigateway.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationSuccessHandler;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebFluxSecurity
public class SpringSecurityConfig {


    @Bean
    public MapReactiveUserDetailsService userDetailsService() {
        UserDetails user = User.withUsername("user")
                .password(passwordEncoder("password"))
                .roles("USER")
                .build();
        UserDetails adminUser = User.withUsername("admin")
                .password(passwordEncoder("admin"))
                .roles("ADMIN")
                .build();
        return new MapReactiveUserDetailsService(user, adminUser);
    }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http.cors()
                .and()
                .formLogin()
                .authenticationSuccessHandler(new RedirectServerAuthenticationSuccessHandler("/home/index.html"))
                .and().authorizeExchange()
                .pathMatchers("/api/career-path/**")
                .permitAll()
                .pathMatchers("/eureka/**").hasRole("ADMIN")
                .anyExchange().authenticated().and()
                .authorizeExchange()
                .pathMatchers("/admin/**")
                .authenticated().and()
                .logout().and().csrf().disable().httpBasic(withDefaults());
        return http.build();
    }

    @Bean
    public String passwordEncoder(String password) {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(password);
    }

}
