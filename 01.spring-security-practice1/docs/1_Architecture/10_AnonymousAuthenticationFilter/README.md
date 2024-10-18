# AnonymousAuthenticationFilter
인증이 안된 유저가 요청을 하면 Anonymous(익명) 유저로 만들어 Authentication에 넣어주는 필터이다.  
인증되지 않았다고 하더라도 Null을 넣는게 아니라 기본 Authentication을 만들어 주는 개념으로 보면 된다.  
다른 Filter에서 Anonymous유저인지 정상적으로 인증된 유저인지 분기 처리를 할 수 있다.

### Anonymous 유저의 SecurityContext
![Filter](../../../images/11.Filter%2010.PNG)

### AnonymousAuthenticationFilter 활성화  
![Filter](../../../images/12.Filter%2011.PNG)
