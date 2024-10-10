
## 01.spring-security-practice1
### 개인 보안 노트 서비스 만들기
* ### 프로젝트 목적  
  SpringSecurity가 필요한 상황을 경험해보고 직접 구현하고, 구현 내용을 토대로 SpringSecurity 아키텍처를 이해하기.
* ### 프로젝트 요구사항  
  1. 유저는 본인의 노트(게시글)만 저장하고 삭제하고 볼 수 있다.
  2. 다른 유저의 노트는 볼 수 없다.
  3. 어드민은 관리 차원에서 유저들의 노트 제목 리스트는 볼 수 있지만 내용은 볼 수 없다.
  4. 어드민은 공지사항을 작성할 수 있고 일반 유저들은 이 공지사항을 볼 수 있다.
* ### 기술 스택
  * Spring WebMVC
  * Spring Security
  * Thymeleaf
  * Lombok
  * Spring JPA
  * H2
  * Gradle