# ThreadLocal
우리가 WebMVC 기반으로 프로젝트를 만든다는 가정하에 대부분의 경우에는 요청 1개에 Thread 1개가 생성된다.
### "요청 1개 : Thread 1개"
이때 ThreadLocal을 사용하면 Thread마다 고유한 공간을 만들 수가 있고 그곳에 SecurityContext를 저장할 수 있다.  
즉, 서버가 어떠한 요청을 받으면 ThreadLocal을 사용해서 그 요청에 해당하는 Security Context를 개별적으로 가질 수 있다.  
요청들을 받았을때 서로서로 영향을 끼치지 않는다.
### "요청 1개 : Thread 1개 : Security Context 1개"
그러나 ThreadLocal만 강제로 사용해야하는 것은 아니며, 원하면 SecurityContext 공유 전략을 바꿀 수 있다.

<br>

### MODE_THREADLOCAL
* ThreadLocalSecurityContextHolderStrategy를 사용합니다.
* ThreadLocal을 사용하여 같은 Thread안에서 SecurityContext를 공유한다.
* 기본 설정 모드.
* 아래의 두 설정은 잘 사용하지 않고 해당 설정을 많이 사용한다.

### MODE_INHERITABLETHREADLOCAL
* InheritableThreadLocalSecurityContextHolderStrategy를 사용한다.
* InheritableThreadLocal을 사용하여 자식 Thread까지도 SecurityContext를 공유한다.

### MODE_GLOBAL
* GlobalSecurityContextHolderStrategy를 사용한다.
* Global로 설정되어 애플리케이션 전체에서 SecurityContext를 공유한다.

<br>

## ThreadLocalSecurityContextHolderStrategy
Spring Security의 기본적인 Security Context 관리 전략은 ThreadLocal을 사용하는 ThreadLocalSecurityContextHolderStrategy이다.

변수는 지역변수, 전역변수와 같은 유효한 Scope를 가진다. 마찬가지로 ThreadLocal은 Thread마다 고유한 영역을 가지고 있는 곳에 저장된 변수로 각각의 Thread안에서 유효한 변수이다.
일반적인 서버의 경우에는 외부로부터 요청이 오면 그 요청마다 Thread 1개가 할당된다. 따라서 ThreadLocal로 SecurityContext를 관리하게 되면 SecurityContext는 요청마다 독립적으로 관리될 수 있다.
```java
final class ThreadLocalSecurityContextHolderStrategy implements SecurityContextHolderStrategy {

	private static final ThreadLocal<SecurityContext> contextHolder = new ThreadLocal<>();

	@Override
	public void clearContext() {
		contextHolder.remove();
	}

	@Override
	public SecurityContext getContext() {
		SecurityContext ctx = contextHolder.get();
		if (ctx == null) {
			ctx = createEmptyContext();
			contextHolder.set(ctx);
		}
		return ctx;
	}

	@Override
	public void setContext(SecurityContext context) {
		Assert.notNull(context, "Only non-null SecurityContext instances are permitted");
		contextHolder.set(context);
	}

	@Override
	public SecurityContext createEmptyContext() {
		return new SecurityContextImpl();
	}

}
```