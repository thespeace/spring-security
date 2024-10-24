package thespeace.practice.spring.security.jwt;

/**
 * <h1>JWT 기본 설정값</h1>
 */
public class JwtProperties {
    public static final int EXPIRATION_TIME = 600000; // 10분
    public static final String COOKIE_NAME = "JWT-AUTHENTICATION";
}
