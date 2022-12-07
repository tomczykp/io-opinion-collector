package pl.lodz.p.it.opinioncollector.userModule.auth;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import pl.lodz.p.it.opinioncollector.userModule.user.UserManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final UserManager userDetailsService;
    private final List<AntPathRequestMatcher> excludedMatchers;


    public JwtFilter(JwtProvider jwtProvider, UserManager userDetailsService) {
        this.jwtProvider = jwtProvider;
        this.userDetailsService = userDetailsService;
        this.excludedMatchers = new ArrayList<>();

        excludedMatchers.add(new AntPathRequestMatcher("/register/**"));
        excludedMatchers.add(new AntPathRequestMatcher("/login"));
        excludedMatchers.add(new AntPathRequestMatcher("/refresh"));
        excludedMatchers.add(new AntPathRequestMatcher("/logout"));
        excludedMatchers.add(new AntPathRequestMatcher("/changePassword"));
        excludedMatchers.add(new AntPathRequestMatcher("/remove/**"));
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return excludedMatchers.stream()
                .anyMatch(antPathRequestMatcher -> antPathRequestMatcher.matches(request));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String jwt = jwtProvider.getToken(request);

        if (jwt != null && jwtProvider.validateToken(jwt)) {
            Claims claims = jwtProvider.parseJWT(jwt).getBody();
            UserDetails userDetails = userDetailsService.loadUserByUsername(claims.getSubject());

            if (!userDetails.isEnabled()) {
                throw new RuntimeException("Account is locked");
            }

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userDetails.getUsername(),
                    null,
                    userDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
        } else {
            throw new RuntimeException();
        }
    }
}
