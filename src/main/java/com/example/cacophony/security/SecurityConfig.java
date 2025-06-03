package com.example.cacophony.security;

import com.example.cacophony.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private JwtAuthFilter authFilter;
    UserDetailsService userService;
    ResourceAccessFilter resourceAccessFilter;
    CacheRequestBodyFilter cacheRequestBodyFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter, UserService userService,
            ResourceAccessFilter resourceAccessFilter, CacheRequestBodyFilter cacheRequestBodyFilter) {
        this.authFilter = jwtAuthFilter;
        this.userService = userService;
        this.resourceAccessFilter = resourceAccessFilter;
        this.cacheRequestBodyFilter = cacheRequestBodyFilter;

    }

    @Bean
    public AuthenticationProvider authenticationProvider(PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(this.userService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return authenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationProvider authenticationProvider)
            throws Exception {
        http.csrf(csrf -> csrf.disable()) // Disable CSRF for stateless APIs
                .authorizeHttpRequests(auth -> auth.requestMatchers("/auth/welcome", "/users", "/users/generateToken")
                        .permitAll().requestMatchers("/actuator/**").permitAll()
                        .requestMatchers("/cacophony/auth/welcome", "/cacophony/users/addNewUser",
                                "/cacophony/users/generateToken")
                        .permitAll().requestMatchers("/auth/user/**").hasAuthority("ROLE_USER")
                        .requestMatchers("/auth/admin/**").hasAuthority("ROLE_ADMIN").anyRequest().authenticated() // Protect
                                                                                                                   // all
                                                                                                                   // other
                                                                                                                   // endpoints
                ).sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS) // No sessions
                ).authenticationProvider(authenticationProvider) // Custom authentication provider
                .addFilterAfter(cacheRequestBodyFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class) // Add JWT filter
                .addFilterAfter(resourceAccessFilter, CacheRequestBodyFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
