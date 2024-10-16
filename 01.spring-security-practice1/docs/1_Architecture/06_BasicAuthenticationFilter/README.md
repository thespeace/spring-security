# Security Filter - BasicAuthenticationFilter
실습을 통해서 어떤 필터인지 알아보자.  
프로젝트를 실행 시킨 뒤 아래처럼 username과 password를 추가해서 curl로 개인노트 페이지를 요청해보자.  
```commandline
curl -u user:user -L http://localhost:8080/note
```

결과는 로그인 페이지로 redirect 시켜준다. 로그인이 되지 않는다.  
이번에는 SpringSecurityConfig에 ```http.httpBasic();``` 추가해서 BasicAuthenticationFilter를 활성화한다.
```java
@Override
protected void configure(HttpSecurity http) thorws Exception {
    // basic authentication
    http.httpBasic();
```
다시 시도해보자.  
```commandline
curl -u user:user -L http://localhost:8080/note
```
이번에는 정상적으로 개인노트 페이지가 불러와진다.

<br>

직접 필터를 적용해보면 따로 로그인이라는 과정을 거치지 않았는데도 일회성으로 페이지를 불러올 수 있다.  
이처럼 우리가 로그인이라고 부르는 과정이 없어도 username: user123 / password : pass123 라는 로그인 데이터를 Base64로 인코딩해서 모든 요청에 포함해서 보내면 BasicAuthenticationFilter는 이걸 인증한다.  
그렇기 때문에 세션이 필요 없고 요청이 올때마다 인증이 이루어 진다.(즉 stateless하다.)  
이런 방식은 요청할 때마다 아이디와 비밀번호가 반복해서 노출되기 때문에 보안에 취약하다.  
그렇기 때문에 BasicAuthenticationFilter를 사용할 때는 반드시 https를 사용하도록 권장된다.

<br>

BasicAuthenticationFilter를 사용하지 않을 것이라면 명시적으로 disable시켜주는 것이 좋다.
```java
@Override
    protected void configure(HttpSecurity http) throws Exception {
        // basic authentication
        http.httpBasic(); // basic authentication filter 활성화
        http.httpBasic().disable(); // basic authentication filter 비활성화
```
