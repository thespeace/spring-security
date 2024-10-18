# RememberMeAuthenticationFilter
RememberMeAuthenticationFilter는 일반적인 세션보다 훨씬 오랫동안 로그인 사실을 기억할 수 있도록 해준다.  
Session의 세션 만료 시간은 기본 설정이 30분이지만 RememberMeAuthenticationFilter의 기본 설정은 2주이다.

![Filter](../../../images/10.Filter%209.PNG)

기본값은 Off이고, 해당 프로젝트는 이미 RememberMeAuthenticationFilter를 사용할 수 있도록 되어있다.

<br>

## 설정 및 구현
```java
//SpringSecurityConfig.java
http.rememberMe();
```

```html
<input type="checkbox" id="remember-me" name="remember-me">
```