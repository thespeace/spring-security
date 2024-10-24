package thespeace.practice.spring.security.config;


import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import thespeace.practice.spring.security.filter.StopwatchFilter;
import thespeace.practice.spring.security.filter.TesterAuthenticationFilter;
import thespeace.practice.spring.security.jwt.JwtAuthenticationFilter;
import thespeace.practice.spring.security.jwt.JwtAuthorizationFilter;
import thespeace.practice.spring.security.jwt.JwtProperties;
import thespeace.practice.spring.security.user.User;
import thespeace.practice.spring.security.user.UserRepository;
import thespeace.practice.spring.security.user.UserService;

/**
 * <h1>Security 설정 Config</h1>
 * <p>해당 프로젝트에서 가장 중요한 클래스</p>
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;
    private final UserRepository userRepository;

    /**
     * <h2>Security 상세 설정 정의</h2>
     * <p>Security의 많은 기능들을 켜고 끄는 설정을 한다.</p>
     *
     * @param http the {@link HttpSecurity} to modify
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //stopwatch filter 추가
//        http.addFilterBefore(
//                new StopwatchFilter(),
//                WebAsyncManagerIntegrationFilter.class
//        );

        //tester authentication filter 추가
//        http.addFilterBefore(
//                new TesterAuthenticationFilter(this.authenticationManager()),
//                UsernamePasswordAuthenticationFilter.class
//        );

        // basic authentication
//        http.httpBasic().disable(); // basic authentication filter 비활성화
        http.httpBasic().disable(); // basic authentication filter 활성화
        //csrf
        http.csrf().disable();
        //remember-me
        http.rememberMe().disable();
        //anonymous
//        http.anonymous();
//        http.anonymous().principal("anonymousUser");
//        http.anonymous().principal(new User());

        // session -> token(JWT) : stateless,
        http.sessionManagement()
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // jwt filter
        http.addFilterBefore(
                new JwtAuthenticationFilter(authenticationManager()),
                UsernamePasswordAuthenticationFilter.class
        ).addFilterBefore(
                new JwtAuthorizationFilter(userRepository),
                BasicAuthenticationFilter.class
        );

        //authorization
        http.authorizeRequests()
                // "/"와 "/home"은 모두에게 허용
                .antMatchers("/","/home","/signup").permitAll()
                // hello 페이지는 USER 룰을 가진 유저에게만 허용
                .antMatchers("/note").hasRole("USER")
                .antMatchers("/admin").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/notice").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/notice").hasRole("ADMIN")
                .anyRequest().authenticated();
        // login
        http.formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/")
                .permitAll(); //모두 허용
        // logout
        http.logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .deleteCookies(JwtProperties.COOKIE_NAME);
    }

    @Override
    public void configure(WebSecurity web) {
        // 정적 리소스 spring security 대상에서 제외
//        web.ignoring().antMatchers("/images/**", "/css/**"); // 아래 코드와 같은 코드.
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    /**
     * <h2>UserDetailsService 구현</h2>
     * <p>Spring Security 에게 User Entity 와 인증 절차 방법을 알려줘야 한다.</p>
     * <p>{@code UserDetailsService.loadUserByUsername()} 구현을 해야한다.</p>
     *
     * @return UserDetailsService
     */
    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        return username -> {
            User user = userService.findByUsername(username);
            if (user == null) {
                throw new UsernameNotFoundException(username);
            }
            return user;
        };
    }

}
