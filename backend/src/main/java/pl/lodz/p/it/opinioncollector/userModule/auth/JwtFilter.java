package pl.lodz.p.it.opinioncollector.userModule.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;
import pl.lodz.p.it.opinioncollector.userModule.user.User;
import pl.lodz.p.it.opinioncollector.userModule.user.UserManager;

import java.io.IOException;
import java.util.Collections;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final UserManager userDetailsService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String jwt = jwtProvider.getToken(request);

        try {
            if (jwt == null || !jwtProvider.validateToken(jwt)) {
                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    Authentication authentication = new UsernamePasswordAuthenticationToken(
                            null,
                            null,
                            Collections.singleton(new SimpleGrantedAuthority("ANONYMOUS")));

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
                filterChain.doFilter(request, response);
                return;
            }
        } catch (ExpiredJwtException exp) {
            if (Objects.equals(request.getRequestURI(), "/api/refresh")
                    || Objects.equals(request.getRequestURI(), "/api/signout")) {
                filterChain.doFilter(request, response);
                return;
            }
            response.setStatus(401);
            return;
        }

        Claims claims = jwtProvider.parseJWT(jwt).getBody();
        User user;

        try {
            user = userDetailsService.loadUserByUsername(claims.getSubject());
        } catch (UsernameNotFoundException enfe) {
            filterChain.doFilter(request, response);
            return;
        }

        if (user.isDeleted()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
        }

        if (user.isLocked()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
        }

        if (!user.isEnabled()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
        }

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user,
                null,
                user.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }
}
