# Spring Security Config 설정하기
## 필터 Off
```http.httpBasic().disable();```  
Spring Security 의 특정 필터를 disable 하여 동작하지 않게 한다.  
사용하지 않을 필터를 명시적으로 disable하는 것도 좋은 방법이다.  

<br>

## 로그인 & 로그아웃 페이지 관련 기능
```java
//login
http.formLogin()
        .loginPage("/login")
        .defaultSuccessUrl("/")
        .permitAll(); // 모두 허용
```
폼 로그인의 로그인 페이지를 지정하고 로그인에 성공했을때 이동하는 URL을 지정 할 수 있다.  

```java
//logout
http.logout()
        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
        .logoutSuccessUrl("/");
```
로그아웃 URL을 지정하고 로그아웃에 성공했을때 이동하는 URL을 지정 할 수 있다.

<br>

## Url Matchers 관련 기능

### antMatchers
```java
http.authorizeRequests()
        .antMatchers("/signup").permitAll()
```
"/signup" 요청을 모두에게 허용.

<br>

### mvcMatchers
```java
http.authorizeRequests()
        .mvcMatchers("/signup").permitAll()
```
"/signup", "/signup/", "/signup.html"와 같은 유사 signup 요청을 모두에게 허용.

<br>

### regexMatchers
정규표현식으로 매칭한다.

<br>

### requestMatchers
antMatchers, mvcMatchers, regexMatchers는 결국에 requestMatchers로 이루어진다.  
명확하게 요청 대상을 지정하는 경우에는 requestMatchers를 사용한다.  
```PathRequest.toStaticResources().atCommonLocations()```

<br>

## 인가 관련 설정
### authorizeRequests()
```http.authorizeRequests()```  
인가를 설정을 시작한다는 뜻.

<br>

### permitAll()
```java
http.authorizeRequests()
        .antMatchers("/home").permitAll()
```
"/home" 요청을 모두에게 허용.

<br>

### hasRole()
```java
http.authorizeRequests()
        .antMatchers(HttpMethod.POST, "/notice").hasRole("ADMIN")
```
특정 권한만 허용 검증.

<br>

### authenticated()
```java
http.authorizeRequests()
        .anyRequest().authenticated()
```
인증이 되었는지를 검증.

<br>

## Ignoring
특정 리소스에 대해서 SpringSecurity자체를 적용하지 않고 싶을때가 있다.  
우리 프로젝트에서는 아래의 css와 png 파일은 굳이 인증 없이 외부에 공개되어 있다.  
이럴때는 ignoring을 사용하면 된다.  

http://localhost:8080/css/signin.css  
http://localhost:8080/images/spring-security.png  

아래 두 개의 코드는 결과적으로 같은 코드이다.  
```java
@Override
public void configure(WebSecurity web) {
    // 정적 리소스 spring security 대상에서 제외
//        web.ignoring().antMatchers("/images/**", "/css/**"); // 아래 코드와 같은 코드.
    web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
}
```

```java
http.authorizeRequests()
        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
```