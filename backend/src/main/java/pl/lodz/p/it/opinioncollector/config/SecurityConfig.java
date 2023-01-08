package pl.lodz.p.it.opinioncollector.config;

import jakarta.servlet.Filter;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
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
    public Filter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http
                .cors().and().csrf().disable()
                .authorizeHttpRequests((auth) -> {
                            try {
                                auth
                                        .requestMatchers(HttpMethod.DELETE, "/users/remove/admin").hasRole("ADMIN")
                                        .requestMatchers(HttpMethod.PUT, "/users/lock").hasRole("ADMIN")
                                        .requestMatchers(HttpMethod.PUT, "/users/unlock").hasRole("ADMIN")
                                        .requestMatchers(HttpMethod.DELETE, "/users/remove/user").hasAnyRole("USER", "ADMIN")
                                        .requestMatchers(HttpMethod.PUT, "/users/password").hasAnyRole("USER", "ADMIN")
                                        .requestMatchers(HttpMethod.DELETE, "/signout/force").hasAnyRole("USER", "ADMIN")

                                        // opinion endpoints
                                        .requestMatchers(HttpMethod.GET, "/products/{productId}/opinions/**").permitAll()
                                        .requestMatchers(HttpMethod.DELETE, "/products/{productId}/opinions/{opinionId}").hasAnyRole("USER", "ADMIN")
                                        .requestMatchers("/products/{productId}/opinions/**").hasRole("USER")

                                        // qa endpoints
                                        .requestMatchers(HttpMethod.GET, "/questions/**").permitAll()
                                        .requestMatchers(HttpMethod.GET, "/answers/**").permitAll()
                                        .requestMatchers(HttpMethod.POST, "/questions/**").hasAnyRole("USER", "ADMIN")
                                        .requestMatchers(HttpMethod.POST, "/answers/**").hasAnyRole("USER", "ADMIN")
                                        .requestMatchers(HttpMethod.DELETE, "/questions/{questionId}").hasAnyRole("USER", "ADMIN")
                                        .requestMatchers(HttpMethod.DELETE, "/answers/{answerId}").hasAnyRole("USER", "ADMIN")

                                        // category and field endpoints
                                        .requestMatchers(HttpMethod.GET, "/category").permitAll()
                                        .requestMatchers(HttpMethod.POST, "/category").hasRole("ADMIN")
                                        .requestMatchers(HttpMethod.GET, "/category/{uuid}").permitAll()
                                        .requestMatchers(HttpMethod.PUT, "/category/{uuid}").hasRole("ADMIN")
                                        .requestMatchers(HttpMethod.DELETE, "/category/{uuid}").hasRole("ADMIN")
                                        .requestMatchers(HttpMethod.GET, "/category/{uuid}/fields").hasRole("ADMIN")
                                        .requestMatchers(HttpMethod.DELETE, "/category/fields/{uuid}").hasRole("ADMIN")
                                        .requestMatchers(HttpMethod.PUT, "/category/fields/{uuid}").hasRole("ADMIN")

                                        // Product endpoints
                                        .requestMatchers(HttpMethod.GET, "/products").permitAll()
                                        .requestMatchers(HttpMethod.GET, "/products/{productId}").permitAll()
                                        .requestMatchers(HttpMethod.GET, "/products/category/{categoryId}").permitAll()
                                        .requestMatchers(HttpMethod.POST, "/products").hasRole("ADMIN")
                                        .requestMatchers(HttpMethod.POST, "/products/suggestion").hasRole("USER")
                                        .requestMatchers(HttpMethod.PUT, "/products/{productId}").hasRole("USER")
                                        .requestMatchers(HttpMethod.PUT, "/products/{productId}/confirm").hasRole("ADMIN")
                                        .requestMatchers(HttpMethod.PUT, "/products/{productId}/unconfirm").hasRole("ADMIN")
                                        .requestMatchers(HttpMethod.PUT, "/products/{productId}/delete").hasRole("USER")
                                        .requestMatchers(HttpMethod.DELETE, "/products/{productId}").hasRole("ADMIN")

                                        // Events endpoints
                                        .requestMatchers(HttpMethod.GET, "/events/{eventID}").hasAnyRole("USER", "ADMIN")
                                        .requestMatchers(HttpMethod.GET, "/user/events").hasAnyRole("USER", "ADMIN")
                                        .requestMatchers(HttpMethod.POST, "/user/eventsCount").hasAnyRole("ADMIN", "USER")
                                        .requestMatchers(HttpMethod.GET, "/events").hasRole("ADMIN")
                                        .requestMatchers(HttpMethod.POST, "/events/{eventID}/close").hasAnyRole("ADMIN", "USER")

                                        // Place for your secured endpoints
                                        .anyRequest().permitAll()
                                        .and()
                                        .oauth2Login().permitAll()
                                        .authorizationEndpoint()
                                        .baseUri("/oauth2/authorize")
                                        .and()
                                        .redirectionEndpoint()
                                        .baseUri("/oauth2/code/*");

                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                ).addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(corsFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
