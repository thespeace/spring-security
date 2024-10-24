package thespeace.practice.spring.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.SigningKeyResolverAdapter;

import java.security.Key;

/**
 * <h1>JwsHeader를 통해 Signature 검증에 필요한 Key를 가져오는 코드를 구현</h1>
 * <ul>
 *     <li>JWT의 헤더에서 kid를 찾아서 Key(SecretKey + 알고리즘)를 찾아온다.</li>
 *     <li>Signature를 검증할 때 사용</li>
 * </ul>
 */
public class SigningKeyResolver extends SigningKeyResolverAdapter {
    public static SigningKeyResolver instance = new SigningKeyResolver();

    @Override
    public Key resolveSigningKey(JwsHeader jwsHeader, Claims claims) {
        String kid = jwsHeader.getKeyId();
        if (kid == null)
            return null;
        return JwtKey.getKey(kid);
    }
}
