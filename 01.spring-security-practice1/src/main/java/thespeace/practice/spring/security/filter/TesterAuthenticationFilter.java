package thespeace.practice.spring.security.filter;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import thespeace.practice.spring.security.user.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <h1>Custom Filter 만들기</h1>
 * <p>테스트 유저("tester*")인 경우에는 어드민과 유저 권한을 모두 부여.</p>
 */
public class TesterAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    public TesterAuthenticationFilter(AuthenticationManager authenticationManager) { super(authenticationManager); }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        Authentication authentication = super.attemptAuthentication(request, response);
        User user = (User) authentication.getPrincipal();
        if(user.getUsername().startsWith("tester")) { // User이름이 tester로 시작하는 경우-
            return new UsernamePasswordAuthenticationToken(
                    user,
                    null,
                    Stream.of("ROLE_ADMIN", "ROLE_USER")
                            .map(authority -> (GrantedAuthority) () -> authority)
                            .collect(Collectors.toList())
            ); // ROLE_ADMIN, ROLE_USER 권한 모두 부여.
        }

        return authentication;
    }
}
