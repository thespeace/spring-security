# Spring Security의 내부 구조
### 그림으로 보는 내부 구조
![Spring Security Internal Structure](../../../images/01.Spring%20Security%20Internal%20Structure.PNG)
### SecurityContextHolder -> SecurityContext -> Authentication -> Principal & GrantAuthority

### 코드로 보는 내부 구조
```java
SecurityContext context = SecurityContextHolder.getContext(); // Security Context
Authentication authentication = context.getAuthentication(); // authentication
authentication.getPrincipal();
authentication.getAuthorities();
authentication.getCredentials();
authentication.getDetails();
authentication.isAuthenticated();
```

<br>

## SecurityContextHolder
* SecurityContextHolder는 SecurityContext를 제공하는 static 메소드(getContext)를 지원한다.

## SecurityContext
* SecurityContext는 접근 주체와 인증에 대한 정보를 담고 있는 Context이다.
* 즉, Authentication 을 담고 있다.

## Authentication
* Principal과 GrantAuthority를 제공한다.
* 인증이 이루어 지면 해당 Authentication이 저장된다.

## Principal
* 유저에 해당하는 정보이다.
* 대부분의 경우 Principal로 UserDetails를 반환한다.

## GrantAuthority
* ROLE_ADMIN, ROLE_USER등 Principal이 가지고 있는 권한을 나타낸다.
* prefix로 'ROLE_'이 붙는다.
* 인증 이후에 인가를 할 때 사용한다.
* 권한은 여러 개일 수 있기 때문에 Collection<GrantedAuthority>형태로 제공한다.
  * ex) ROLE_DEVELOPER, ROLE_ADMIN

