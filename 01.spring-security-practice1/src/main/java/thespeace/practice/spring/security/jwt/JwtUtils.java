package thespeace.practice.spring.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import org.springframework.data.util.Pair;
import thespeace.practice.spring.security.user.User;

import java.security.Key;
import java.util.Date;

public class JwtUtils {

    /**
     * <h2>토큰에서 username 찾기</h2>
     *
     * @param token 토큰
     * @return username
     */
    public static String getUsername(String token) {
        // jwtToken에서 username을 찾는다.
        return Jwts.parserBuilder()
                .setSigningKeyResolver(SigningKeyResolver.instance)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject(); // username
    }

    /**
     * <h2>user로 토큰 생성</h2>
     * <ul>
     *     <li>HEADER : alg, kid</li>
     *     <li>PAYLOAD : sub, iat, exp</li>
     *     <li>SIGNATURE : JwtKey.getRandomKey로 구한 Secret Key로 HS512 해시</li>
     * </ul>
     *
     * @param user 유저
     * @return jwt token
     */
    public static String createToken(User user) {
        Claims claims = Jwts.claims().setSubject(user.getUsername()); // subject
        Date now = new Date(); // 현재 시간
        Pair<String, Key> key = JwtKey.getRandomKey();

        // JWT Token 생성
        return Jwts.builder()
                .setClaims(claims) // 위에서 정의한 Claims 정보 저장
                .setIssuedAt(now) // 토큰 발행 시간 정보
                .setExpiration(new Date(now.getTime() + JwtProperties.EXPIRATION_TIME)) // 토큰 만료 시간 설정
                .setHeaderParam(JwsHeader.KEY_ID, key.getFirst()) // kid
                .signWith(key.getSecond()) // signature
                .compact();
    }
}
