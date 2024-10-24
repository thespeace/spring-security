package thespeace.practice.spring.security.jwt;


import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import thespeace.practice.spring.security.user.User;
import thespeace.practice.spring.security.user.UserRepository;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

/**
 * <h1>JWT를 이용한 인증 필터</h1>
 * <ol>
 *     <li>Cookie에서 JWT Token을 구한다.</li>
 *     <li>JWT Token을 파싱하여 username을 구한다.</li>
 *     <li>username으로 User를 구하고 Authentication을 생성한다.</li>
 *     <li>생성된 Authentication을 SecurityContext에 넣는다.</li>
 *     <li>Exception이 발생하면 응답의 쿠키를 null로 변경한다.</li>
 * </ol>
 */
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;

    public JwtAuthorizationFilter(
            UserRepository userRepository
    ) {
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain
    ) throws IOException, ServletException {
        String token = null;
        try {
            // cookie 에서 JWT token을 가져옵니다.
            token = Arrays.stream(request.getCookies())
                    .filter(cookie -> cookie.getName().equals(JwtProperties.COOKIE_NAME))
                    .findFirst()
                    .map(Cookie::getValue)
                    .orElse(null);
        } catch (Exception ignored) {
            // 아무것도 하지 않는다.
        }
        if (token != null) {
            try {
                // authentication을 만들어서 SecurityContext에 넣어준다.
                Authentication authentication = getUsernamePasswordAuthenticationToken(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception e) {
                // 실패하는 경우에는 쿠키를 초기화한다.
                Cookie cookie = new Cookie(JwtProperties.COOKIE_NAME, null);
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
        }
        chain.doFilter(request, response);
    }

    /**
     * <h2>새로운 authentication을 만드는 메서드</h2>
     * <p>JWT 토큰으로 User를 찾아서 UsernamePasswordAuthenticationToken를 만들어서 반환한다.</p>
     * <p>User가 없다면 null</p>
     */
    private Authentication getUsernamePasswordAuthenticationToken(String token) {
        // 토큰으로 username을 찾는다.
        String userName = JwtUtils.getUsername(token);
        if (userName != null) {
            // username으로 User를 찾는다.
            User user = userRepository.findByUsername(userName); // 유저를 유저명으로 찾습니다.
            return new UsernamePasswordAuthenticationToken(
                    user, // principal
                    null,
                    user.getAuthorities()
            );
        }
        return null; // 유저가 없으면 NULL
    }
}