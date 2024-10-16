# Security Filter
Spring Security의 동작은 사실상 Filter로 동작한다고 해도 무방하다.  
다양한 필터들이 존재하는데 이 Filter들은 각자 다른 기능을 하고 있다.  
이런 Filter들은 제외할 수도 있고 추가할 수도 있다. 필터에 동작하는 순서를 정해줘서 원하는대로 유기적으로 동작할 수 있다.  

필터의 종류는 많지만 많이 쓰이는 필터는 아래와 같다.
* SecurityContextPersistenceFilter
* BasicAuthenticationFilter
* UsernamePasswordAuthenticationFilter
* CsrlFilter
* RememberMeAuthenticationFilter
* AnonymousAuthenticationFilter
* FilterSecurityInterceptor
* ExceptionTranslationFilter

<br>

## Filter
SecurityContextPersistenceFilter를 보면 아래처럼 GenericFilterBean를 상속하고 있고 GenericFilterBean는 Filter를 상속하고 있다.  
즉, SecurityContextPersistenceFilter는 Filter를 구현한다.  
```java
public class SecurityContextPersistenceFilter extends GenericFilterBean (
public abstract class GenericFilterBean implements Filter
```
SpringSecurity에는 다양한 Filter들이 존재하는데 그 중에 하나가 Security ContextPersistenceFilter 일 뿐이다.  
Filter들의 종류와 기능들을 알기에 앞서 Filter가 무엇인지 파악해보자.

필터는 요청이나 응답 또는 둘 다에 대해 필터링 작업을 수행하는 개체이다.  
필터는 doFilter 메소드에서 필터링을 수행한다. 즉 Filter는 doFilter를 구현해야한다.
```java
public interface Filter {
    public default void init (FilterConfig filterConfig) throws ServletException {}
    public void doFilter(ServletRequest request, ServletResponse response, 
                         FilterChain chain) throws IOException, ServletException;
    public default void destroy(){}
}
```

<br>

간단히 생각하면 요청 전, 응답 후 어떤 작업을 하도록 하는게 Filter라고 생각하면 된다.  
![Filter](../../../images/02.Filter%201.PNG)

Filter가 여러 개인 상황이면 아래처럼 동작한다.  
마지막 순서 필터가 안쪽부터 첫번째 필터의 가장 밖까지 감싸고 있는 형태라고 볼 수 있다.  
![Filter](../../../images/03.Filter%202.PNG)

<br>

## Security Filter
다양한 필터들과 그 필터들의 동작 순서는 FilterChainProxy Class 에서 doFilterInternal에 break point를 걸어서 디버깅해보면 알 수 있다.  
아래 그림에는 12가지의 필터가 적용된 것을 볼 수 있는데 우리가 구현했던 SpringSecurityConfig를 통해서 어떤 필터가 적용되는지 선택된다.  
![Filter](../../../images/04.Filter%203.PNG)
