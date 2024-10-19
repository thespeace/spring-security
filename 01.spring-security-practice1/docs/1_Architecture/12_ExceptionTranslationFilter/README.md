# ExceptionTranslationFilter
앞서 본 FilterSecurityInterceptor에서 발생할 수 있는 두가지 Exception을 처리해주는 필터이다.  
1. AuthenticationException : 인증에 실패할 때 발생
2. AccessDeniedException : 인가에 실패할 때 발생
즉, 인증이나 인가에 실패했을 때 어떤 행동을 취해야하는지를 결정해주는 Filter이다.

ExceptionTranslationFilter의 handleSpringSecurityException는 Exception의 종류에 따른 로직을 분산한다.  
```java
private void handleSpringSecurityException(HttpServletRequest request, HttpServletResponse response,
			FilterChain chain, RuntimeException exception) throws IOException, ServletException {
		if (exception instanceof AuthenticationException) {
			handleAuthenticationException(request, response, chain, (AuthenticationException) exception);
		}
		else if (exception instanceof AccessDeniedException) {
			handleAccessDeniedException(request, response, chain, (AccessDeniedException) exception);
		}
	}
```

<br>

## 기본 설정
* AuthenticationException 발생 또는 Anonymous의 AccessDeniedExcepiton 발생 : Login Page로 이동
* AccessDeniedException 발생 : 403 Forbidden Whitelabel Error Page로 이동

Exception에 대한 대처 방식을 변경할 수 있다.  
![Filter](../../../images/14.Filter%2013.PNG)

정리하자면, ExceptionTranslationFilter는 최종적으로 발생한 인증과 인가의 실패에 대한 후처리를 하는 필터이다.