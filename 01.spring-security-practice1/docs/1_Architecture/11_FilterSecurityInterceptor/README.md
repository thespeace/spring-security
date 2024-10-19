# FilterSecurityInterceptor
이름만 봐서는 Interceptor로 끝나지만 Filter 중에 하나이다.  
앞서 본 SecurityContextPersistenceFilter, UsernamePasswordAuthenticationFilter, AnonymousAuthenticationFilter 에서 SecurityContext 를 찾거나 만들어서 넘겨주고 있다는 걸 확인했다.  
FilterSecurityInterceptor 에서는 이렇게 넘어온 authentication 의 내용을 기반으로 최종 인가 판단을 내린다.  
그렇기 때문에 대부분의 경우에는 필터 중에 뒤쪽에 위치 한다.  
먼저, 인증(Authentication)을 가져오고 만약에 인증에 문제가 있다면 AuthenticationException 이 발생한다.  
인증에 문제가 없다면 해당 인증으로 인가를 판단한다.  
이때 인가가 거절된다면 AccessDeniedException 를 발생하고 승인된다면 정상적으로 필터가 종료된다.  

![Filter](../../../images/13.Filter%2012.PNG)

정리하자면 FilterSecurityInterceptor는 인증과 인가에 대한 최종 판단을 내린다.  
그 과정에서 문제가 생긴다면 AuthenticationException, AccessDeniedException이 발생시킨다.