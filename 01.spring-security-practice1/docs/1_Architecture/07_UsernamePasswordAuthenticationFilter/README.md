# UsernamePasswordAuthenticationFilter
UsernamePasswordAuthenticationFilter는 Form 데이터로 username, password 기반의 인증을 담당하고 있는 필터이다.

![Filter](../../../images/07.Filter%206.PNG)

<br>

## ProviderManager (AuthenticationManager)
인자로 받은 authentication이 유효한지 확인하고 authentication을 반환한다.  
인증하면서 계정에 문제가 있는 것이 발견되면 AuthenticationException를 throw할 수 있다.  
AuthenticationManager는 authenticate 하나만 구현하면 된다.

```java
public interface AuthenticationManager {
    Authentication authenticate(Authentication authentication) throws AuthenticationException
}
```

이런 AuthenticationManager를 구현한 Class가 ProviderManager이다.  
ProviderManager는 Password가 일치하는지, 계정이 활성화 되어있는지를 확인한 뒤 authentication을 반환한다.  
```java
public class ProviderManager implements AuthenticationManager, MessageSourceAware, InitializingBean {
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 생략 ...
        result = provider.authenticate(authentication);
        // 생략 ...
    }
}
```
