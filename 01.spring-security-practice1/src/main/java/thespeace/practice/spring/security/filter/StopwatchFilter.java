package thespeace.practice.spring.security.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <h1>Custom Filter 만들기</h1>
 * <p>프로젝트에 SpringSecurity를 포함시켜 개발하다 보면 SpringSecurity에서
 * 기본으로 제공하는 필터뿐만 아니라 개발자가 원하는 방식대로 동작하는 필터가 필요할때가 많다.</p>
 * <p>이럴때 우리는 커스텀 필터를 구현하면 된다.</p>
 * <p>커스텀 필터를 구현하기 위해서는 다른 필터와 마찬가지로 Filter Interface를 구현해야 한다.</p>
 * <p>그러나 Filter Interface를 직접 구현하게 되면 중복실행 문제가 있어서 대부분의 경우에는
 * OncePerRequestFilter를 구현하기를 권장한다.</p>
 * <br>
 * <p>해당 클래스는 1개의 요청이 들어온 시점부터 끝날 때 까지 걸린 시간을 Log로 기록하는
 * StopwatchFilter를 만들어보자.</p>
 * <p>ex) StopWatch '/login': running time = 150545041 ms</p>
 */
@Slf4j
public class StopwatchFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        StopWatch stopWatch = new StopWatch(request.getServletPath());
        stopWatch.start(); // stop watch 시작
        filterChain.doFilter(request, response);
        stopWatch.stop(); // stop watch 종료
        // Log StopWatch '/login': running time = 150545041 ms
        log.info(stopWatch.shortSummary());
    }
}
