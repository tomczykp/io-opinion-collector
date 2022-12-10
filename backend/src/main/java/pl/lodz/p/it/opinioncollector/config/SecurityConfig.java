package pl.lodz.p.it.opinioncollector.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
                        .requestMatchers("/users/password").authenticated()
                        .requestMatchers("/users/remove/admin").hasRole("ADMIN")
                        .requestMatchers("/users/lock").hasRole("ADMIN")
                        .requestMatchers("/users/unlock").hasRole("ADMIN")
                        .requestMatchers("/users/remove/user").hasRole("USER")
                        .requestMatchers("/signout/force").authenticated()
                        .requestMatchers("/users/remove/admin").authenticated()
                        .requestMatchers("/users/lock").authenticated()
                        .requestMatchers("/users/unlock").authenticated()
                        .anyRequest().permitAll()
                ).addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
        //If you add paths here, remember to add it also in JwtFilter
    }
}
