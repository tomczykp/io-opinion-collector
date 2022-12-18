package pl.lodz.p.it.opinioncollector.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pl.lodz.p.it.opinioncollector.userModule.auth.JwtFilter;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;


    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http,
                                                       PasswordEncoder passwordEncoder,
                                                       UserDetailsService userDetailsService) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder)
                .and()
                .build();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http
                .csrf().disable().cors().and()
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers(HttpMethod.DELETE, "/users/remove/admin").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/users/lock").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/users/unlock").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/users/remove/user").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/users/password").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/signout/force").authenticated()
                        // opinion endpoints
                        .requestMatchers(HttpMethod.GET, "/products/{productId}/opinions/**").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/products/{productId}/opinions/**").authenticated()
                        // Place for your secured endpoints
                        .anyRequest().permitAll()
                ).addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
        //If you add paths here, remember to add it also in JwtFilter
    }
}
