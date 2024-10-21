# Spring Security 테스트하기
SpringSecurity를 사용하는 프로젝트의 테스트는 SpringSecurity가 없는 프로젝트의 테스트와 조금 다른 부분이 있다.  
SpringSecurity의 테스트에서는 User가 로그인한 상태를 가정하고 테스트해야 하는 경우가 많다.  
인증을 받지 않은 상태로 테스트를 하면 SpringSecurity에서 요청 자체를 막기 때문에 테스트가 제대로 동작조차 하지 못한다.  
이런 문제는 프로젝트에 spring-security-test를 사용해서 해결할 수 있다.  
spring-security-test를 사용하면 테스트 직전에 Mock User를 인증시켜놓고 테스트를 구동시킬수 있다.  

<br>

SpringSecurityTest 의존성 추가  
```testImplementation 'org.springframework.security:spring-security-test'```

<br>

Test 실행 전 MockMvc에 springSecurity (static 메서드)를 설정하자.  
```java
@BeforeEach
public void setUp(@Autowired WebApplicationContext applicationContext) {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext)
        .apply(springSecurity()) //spring security 적용
        .build();
}
```

<br>

## @WithMockUser
Mock(가짜) User를 생성하고 Authentication을 만든다.  
여기서 User는 org.springframework.security.core.userdetails.User를 말한다.  


|     멤버 변수     |                                   예시                                   |          설명           |
|:-------------:|:----------------------------------------------------------------------:|:---------------------:|
|     roles     |                                  USER                                  |   권한(ROLE_ 자동으로 붙음)   |
|  authorities  |                               ROLE_USER                                |  권한(사용하면 roles를 무시함)  |
|   username    |                                user123                                 |          유저명          |
|   password    |                              password123                               |         패스워드          |
|  setupBefore  |  TestExecutionEvent.TEST_METHOD<br/>TestExecutionEvent.TEST_EXECUTION  |    언제 유저가 세팅되는지 정함    |

내부에서 UserDetails를 직접 구현해서 Custom User를 만들어 사용하는 경우에는 WithMockUser를 사용하면 문제가 발생 할 수 있다.  
WithMockUser는 org.springframework.security.core.userdetails.User를 만들어 주지만 우리가 필요한 User는 Custom User이기 때문이다.(class cast 에러가 발생할 수 있다.)

<br>

## @WithUserDetails
WithMockUser와 마찬가지로 Mock(가짜) User를 생성하고 Authentication을 만들어 준다.  
WithMockUser와 다른점은 가짜 User를 가져올 때 UserDetailsService의 Bean 이름을 넣어줘서 userDetailsService.loadUserByUsername(String username)을 통해 User를 가져온다.

|            멤버 변수             |                 예시                  |                 설명                 |
|:----------------------------:|:-----------------------------------:|:----------------------------------:|
|            value             |               user123               |         가져올 user의 username         |
|  userDetailsServiceBeanName  |         userDetailsService          |  UserDetailsService를 구현한 Bean의 이름  |
|         setupBefore          |  TestExecutionEvent.TEST_EXECUTION  |          언제 유저가 세팅되는지 정함           |

<br>

## @WithAnonymousUser
WithMockUser와 동일하지만 인증된 유저 대신에 익명(Anonymous)유저를 Authentication에서 사용한다.  
익명이기 때문에 멤버 변수에 유저와 관련된 값은 없다.

| 멤버 변수       | 예시                                | 설명              |
|-------------|-----------------------------------|-----------------|
| setupBefore | TestExecutionEvent.TEST_EXECUTION | 언제 유저가 세팅되는지 정함 |

<br>

## @WithSecurityContext
다른 방식들은 Authentication을 가짜로 만들었다고 한다면 WithSecurityContext는 아예 SecurityContext를 만든다.

| 멤버 변수       | 설명                                                                                                                                                                                                           |
|-------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| factory     | WithSecurityContextFactory를 Implement한 Class를 넣어준다.<br/>```public interface WithSecurityContextFactory<A extends Annotation> {```<br/>```SecurityContext createSecurityContext(A annotation);```<br/>```}``` |
| setupBefore | 언제 유저가 세팅되는지 정함                                                                                                                                                                                              |

<br>

## with(user(  ))
다른 방식은 어노테이션 기반인 반면에 이 방식은 직접 User를 MockMvc에 주입하는 방법이다.  
WithMockUser와 마찬가지로 유저를 생성해서 Principal에 넣고 Authentication을 생성해준다.  
org.springframework.security.test.web.servlet.request.user를 사용한다.
```java
mockMvc.perform(get("/admin")
        .with(user(user))) //유저 추가
```