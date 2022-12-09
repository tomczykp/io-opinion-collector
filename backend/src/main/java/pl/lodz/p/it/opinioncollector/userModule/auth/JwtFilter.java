package pl.lodz.p.it.opinioncollector.userModule.auth;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;
import pl.lodz.p.it.opinioncollector.userModule.user.UserManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final UserManager userDetailsService;
    private final List<AntPathRequestMatcher> securePaths;


    public JwtFilter(JwtProvider jwtProvider, UserManager userDetailsService) {
        this.jwtProvider = jwtProvider;
        this.userDetailsService = userDetailsService;
        this.securePaths = new ArrayList<>();

        //Add secure paths here, and then in SecurityConfig set who can access certain endpoints
        securePaths.add(new AntPathRequestMatcher("/users/password"));
        securePaths.add(new AntPathRequestMatcher("/users/remove/**"));
        securePaths.add(new AntPathRequestMatcher("/signout/force"));
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return securePaths.stream()
                .noneMatch(path -> path.matches(request));
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
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }
}
