package school.sorokin.event_notificator.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import school.sorokin.event_notificator.exception.AuthenticationException;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    private static final int NUMBER_FOR_THE_SAMPLE_BEARER_FROM_LINE_JST = 7;

    private static final Logger log = LoggerFactory.getLogger(JwtTokenFilter.class);

    private final JwtTokenManager jwtTokenManager;

    public JwtTokenFilter(JwtTokenManager jwtTokenManager) {
        this.jwtTokenManager = jwtTokenManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        log.info("start check jwt token");
        try {
            String jwtToken = getTokenFromRequest(request);
            log.info("toke is {}", jwtToken);
            if (jwtToken != null) {
                String login = jwtTokenManager.getLoginFromToken(jwtToken);
                List<String> roles = jwtTokenManager.getRolesFromToken(jwtToken);
                Collection<? extends GrantedAuthority> authorities =
                        roles.stream()
                                .map(SimpleGrantedAuthority::new)
                                .toList();

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        login,
                        null,
                        authorities
                );
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        } catch (Exception e) {
            log.error("Cannot set user authentication", e);
        }

        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String authHerder = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(authHerder) && authHerder.startsWith("Bearer ")) {
            return authHerder.substring(NUMBER_FOR_THE_SAMPLE_BEARER_FROM_LINE_JST);
        }
        throw new AuthenticationException("Authorization header is missing or invalid");
    }
}
