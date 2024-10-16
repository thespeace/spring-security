# PasswordEncoder

## Password를 안전하게 관리하자
우리는 Spring Security를 사용하면서 유저의 Password를 관리해야할 필요가 있다.
Password를 관리할 때는 일단 두 가지가 만족되어야 한다.
1. 회원가입할 때 Password를 입력받으면 그 값을 암호화해서 저장해야 한다.
2. 로그인할 때 입력받은 Password와 회원가입할 때의 Password를 비교할 수 있어야 한다.

이 두 가지를 만족하기 위해서는 보통 해시 함수라는 알고리즘 방식을 이용한다.  
해시 함수는 암호화는 비교적 쉽지만 복호화가 거의 불가능한 방식의 알고리즘이다.  
이것을 사용하면 아래와 같은 방식으로 password를 관리할 수 있다.
1. 회원가입할 때 password를 해시함수로 암호화해서 저장
2. 로그인할 때 password가 들어오면 같은 해시함수로 암호화
3. 저장된 값을 불러와서 2번의 암호화된 값과 비교
4. 동일하면 같은 암호로 인지

<br>

## PasswordEncoder 인터페이스
```java
public interface PasswordEncoder {

	/**
	 * @param rawPassword 평문 패스워드
     * @return 암호화된 패스워드   
	 */
	String encode(CharSequence rawPassword);

	/**
	 * @param rawPassword 평문 패스워드
     * @param encodedPassword 암호화된 패스워드
     * @return rawPassword를 암호화한 값과 encodedPassword의 일치 여부
	 */
	boolean matches(CharSequence rawPassword, String encodedPassword);

}
```

<br>

## PasswordEncoder 전략

| 종류                    | 예시                                                                                                                                                    |
|-----------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------|
| NoOpPasswordEncoder   | {noop}password                                                                                                                                        |
| BcryptPasswordEncoder | {bcrypt}$2a$10$dXJ3SW6G7P50IGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG                                                                                  |
| StandardPasswordEncoder   | {sha256}97cde38028ad898ebc02e690819fa220e88c62e0699403e94fff291cfffaf8410849f27605abcbc0                                                              |
| Pbkdf2PasswordEncoder   | {pbkdf2}5d923b44a6d129f3ddf3e3c8d29412723dcbde72445e8ef6bf3b508fbf17fa4ed4d6b99ca763d8dc                                                              |
| SCryptPasswordEncoder   | {scrypt}$e0801$8bWJaSu2IKSn9Z9kM+TPXfOC/<br/>9bdYSrN10D9qfVThWEwdRTnO7re7Ei+fUZRJ68k9ITyuTeUp4of4g24hHnazw==$OAOec05+bXxvuu<br/>1qZ6NUR+xQYvYv7BeL1QxwRpY5Pc= |

* 스프링 시큐리티는 DelegatingPasswordEncoder라는 표의 모든 PasswordEncoder를 선택할 수 있는 대표 PasswordEncoder를 따로 만들어서 사용하고 있다.
* 예시를 보면 뒤쪽의 난해한 문자들은 암호화된 값 같이 보인다.
* 그런데 {noop} {bcrypt} {sha256} {pbkdf2} {scrypt} 같은 앞의 단어들은 무엇일까?
* DelegatingPasswordEncoder는 어떤 Password Encoder를 선택했는지 알려주기 위해서 앞에 암호화 방식을 표현하고 있다. 그렇기 때문에 어떤 암호는 bcrypt로 암호화되고 다른 암호는 sha256 되었다고 하더라도 DelegatingPasswordEncoder는 둘 다 지원할 수 있다.
* 참고로 DelegatinaPassword Encoder는 인코딩 전략으로 Bcrypt를 기본 Encoder로 사용하고 있다.

<br>

## PasswordEncoder 종류
* ### NoOpPasswordEncoder
  * 암호화하지 않고 평문으로 사용한다.
  * password가 그대로 노출되기 때문에 현재는 deprecated 되었고 사용하지 않기를 권장한다.

* ### BcryptPasswordEncoder
  * Bcrypt 해시 함수를 사용한 PasswordEncoder이다.
  * Bcrypt는 애초부터 패스워드 저장을 목적으로 설계되었다.
  * Password를 무작위로 여러번 시도하여 맞추는 해킹을 방지하기 위해 암호를 확인할 때 의도적으로 느리게 설정되어있다.
  * BcryptPasswordEncoder는 강도를 설정할 수 있는데 강도가 높을수록 오랜 시간이 걸린다.

* ### Pbkdf2PasswordEncoder
  * Pbkdf2는 NIST(National Institute of Standards and Technology, 미국표준기술연구소)에 의해서 승인된 알고리즘이고, 미국 정부 시스템에서도 사용한다.

* ### ScryptPasswordEncoder
  * Scrypt Pbkdf2와 유사하다.
  * 해커가 무작위로 password를 맞추려고 시도할 때 메모리 사용량을 늘리거나 반대로 메모리 사용량을 줄여서 느린 공격을 실행할 수밖에 없도록 의도적인 방식을 사용한다.
  * 따라서 공격이 매우 어렵고 Pbkdf2보다 안전하다고 평가받는다.
  * 보안에 아주 민감한 경우에 사용할 수 있다.