# JWT
JWT는 토큰 기반 인증 시스템의 구현체 토큰 방식에서 가장 잘 알려져 있고, Json Web Token의 줄임말이다.

<br>

## JWT 기본 정보
* 웹표준 (RFC 7519) 으로서 두 개체에서 JSON 객체를 사용하여 가볍고 자가수용적인 (self-contained) 방식으로 정보를 안전성 있게 전달할 수 있다.  

### 수 많은 프로그래밍 언어에서 지원된다.
* JWT는 C, Java, Python, C++, R, C#, PHP, JavaScript, Ruby, Go, Swift 등 대부분의 주류 프로그래밍 언어에서 지원된다.

### 자가 수용적(self-contained)이다.
* JWT는 필요한 모든 정보를 자체적으로 지니고 있다.
* JWT 시스템에서 발급된 토큰은, 토큰에 대한 기본정보, 전달 할 정보 ( ex)로그인 시스템에서는 유저 정보) 그리고 토큰이 검증됐다는 것을 증명해주는 signature를 포함하고 있다.

### 쉽게 전달 할 수 있다.
* JWT는 자가수용적이므로, 두 개체 사이에서 손쉽게 전달 될 수 있다.
* 웹서버의 경우 HTTP의 헤더에 넣어서 전달 할 수도 있고, URL 의 파라미터로 전달 할 수도 있다.

<br>

## JWT 구조  
JWT는 ```.```을 구분자로 3가지의 문자열로 구성되어 있다.  
구조는 다음과 같다.
```
aaaaaa.bbbbbbb.ccccccccc
HEADER.PAYLOAD.SIGNATURE
```

<br>

### Header
HEADER는 JWT를 검증하는데 필요한 정보를 가진 객체이다.  
Signature에 사용한 암호화 알고리즘이 무엇인지, Key의 ID가 무엇인지 정보를 담고 있다.  
이 정보를 Json으로 변환해서 UTF-8로 인코딩한 뒤 Base64 URL-Safe로 인코딩한 값이 들어가 있다.  
결과 값이 난해한 문자로 보이지만 암호화된 값은 아니다.  
```json
{
  "alg" : "HS512",
  "kid" : "key1"
}
// -> eyJhbGciOiJlUzUxMilslmtpZcl6lmteTElfQ
```

<br>

### Payload
실질적으로 인증에 필요한 데이터를 저장한다.  
데이터의 각각 필드들을 **Claim**이라고 한다.  
대부분의 경우에 Claim에 username을 포함한다.  
인증할 때 payload에 있는 username을 가져와서 유저 정보를 조회할 때 사용해야하기 때문이다.  
또한, 토큰 발행시간(iat)와 토큰 만료시간(exp)를 포함한다.  
그 외에도 원하는 Claim을 얼마든지 추가할 수 있지만 민감정보는 포함시켜서는 안된다.  
Payload 역시 Header와 마찬가지로 암호화되지 않는다.  
Json으로 바꾼뒤 UTF8로 인코딩하고 Base64로 변경한 데이터일 뿐이다.
```json
{
  "sub" : "user",
  "iat" : 1629626974,
  "exp" : 1629627574
}
// -> eyJzdWli1ifnlafnwef3fakenfovnziononi3onot2gfa89
```

<br>

### Signature
앞선 Header와 Payload는 암호화하지 않았고 json -> utf8 -> base64로 변환한 데이터이다.  
이렇게 Header와 Payload 생성 메커니즘은 너무 쉽고 누구나 만들 수 있는 데이터이다.  
따라서 저 두개의 데이터만 있다면 토큰에 대한 진위여부 판단은 전혀 이루어질수 없다.  
그래서 JWT의 구조에서 가장 마지막에 있는 Signature는 토큰 자체의 진위여부를 판단하는 용도로 사용한다.  
Signature는 Header와 Payload를 합친 뒤 비밀키로 Hash를 생성하여 암호화한다.

```Header, Payload 값을 SecretKey로 Hashing하고 Base64로 변경```한다.  
![JWT](../../../images/18.JWT%204.PNG)

<br>

## Key Rolling
JWT의 토큰 생성 메커니즘을 보다보면 Secret Key가 노출되면 사실상 모든 데이터가 유출될 수 있다는 걸 알 수 있다.  
이런 문제를 방지하기 위해서 Secret Key를 여러개 두고 Key 노출에 대비할 수 있다.  
Secret Key를 여러 개를 사용하고 수시로 추가하고 삭제해줘서 변경한다면 SecretKey 중에 1개가 노출되어도 다른 Secret Key와 데이터는 안전한 상태가 된다.  
이걸 바로 Key Rolling이라 한다.(Key Rolling이 필수는 아니다.)

Key Rolling에서는 여러 개의 Secret Key가 존재한다.  
Secret Key 1개에 Unique한 ID(kid 혹은 key id라고 부름)를 연결시켜 두고, JWT 토큰을 만들 때 헤더에 kid를 포함하여 제공하고 서버에서 토큰을 해석할 때 kid로 Secret Key를 찾아서 Signature를 검증한다.  
