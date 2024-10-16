# Security Filter - SecurityContextPersistenceFilter
SecurityContextPersistenceFilter는 보통 두번째로 실행되는 필터이다.  
(첫번째로 실행되는 필터는 Async 요청에 대해서도 SecurityContext를 처리할 수 있도록 해주는 WebAsyncManagerIntegrationFilter 이다.)

SecurityContextPersistenceFilter는 SecurityContext를 찾아와서 SecurityContextHolder에 넣어주는 역할을 하는 Filter이다.
만약에 SecurityContext를 찾았는데 없다면 그냥 새로 하나 만들자.
```java
public class SecurityContextPersistenceFilter extends GenericFilterBean {
    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        SecurityContext contextBeforeChainExecution = this.repo.loadContext(holder); //httpSession에서 Context를 가져온다.
        try {
            SecurityContextHolder.setContext(contextBeforeChainExecution); //**
            chain.doFilter(holder.getRequest(), holder.getResponse()); //**
        }
        finally {
            SecurityContext contextAfterChainExecution = SecurityContextHolder.getContext();
            // Crucial removal of SecurityContextHolder contents before anything else.
            SecurityContextHolder.clearContext();
            this.repo.saveContext(contextAfterChainExecution, holder.getRequest(), holder.getResponse());
        }
    }
}
```

<br>

## HttpSession
SecurityContextPersistenceFilter는 SecurityContext가 있으면 그걸 사용, 가져오고 없으면 새로 만든다.    
그럼 가져온다는건 대체 어디서 가져온다는 걸까?  
가져올 수 있는 방법은 많지만, 기본적으로는 HttpSession에서 가져온다.
![Filter](../../../images/05.Filter%204.PNG)

<br>

## JSESSIONID
세션 유지에 필요한 Session ID를 쿠키로 가지고 있어야 한다.  
그 값은 JSESSIONID라는 key에 넣어서 가지고 있다.  
![Filter](../../../images/06.Filter%205.PNG)  
크롬의 확장프로그램 EditThisCookie를 통해 확인한 쿠키  
(https://chromewebstore.google.com/detail/editcookie/eognaopbbjmpompmibmllnddafjhbfdj?hl=ko)