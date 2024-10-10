package thespeace.practice.spring.security.user;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Collection;
import java.util.Collections;

/**
 * <h1>User Entity</h1>
 * <p>Spring Security에서 인증과 인가를 할때 사용하는 UserDetails를 구현하는 것이 중요하다.</p>
 *
 * <ul><h3>Methods in UserDetails</h3>
 *     <li>getUsername(): 로그인시 사용하는 아이디</li>
 *     <li>getPassword(): 로그인시 사용하는 비밀번호</li>
 *     <li>getAuthorities(): 권한 목록</li>
 *     <li>isAccountNonExpired(): 계정 만료 여부(boolean)</li>
 *     <li>isAccountNonLocked(): 계정 Lock 여부(boolean)</li>
 *     <li>isCredentialsNonExpired(): 비밀번호의 만료 여부(boolean)</li>
 *     <li>isEnabled(): 계정 유효 여부(boolean)</li>
 * </ul>
 *
 * <p>해당 예시에서는 메서드들을 간단히 작성했지만, 추후에 계정상태를 상세히 관리하려면 DB 컬럼으로 관리하면 된다.</p>
 */
@Entity
@Table
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User implements UserDetails {

    @GeneratedValue
    @Id
    private Long id;
    private String username;
    private String password;
    private String authority;

    public User(
            String username,
            String password,
            String authority
    ) {
        this.username = username;
        this.password = password;
        this.authority = authority;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton((GrantedAuthority) () -> authority);
    }

    public Boolean isAdmin() {
        return authority.equals("ROLE_ADMIN");
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
