package thespeace.practice.spring.security.note;

import thespeace.practice.spring.security.user.User;
import thespeace.practice.spring.security.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * <h1>MockMvc에 Spring Security Test를 연동해서 함께 사용</h1>
 * <p>스프링 시큐리티의 테스트는 일반적인 컨트롤러 테스트와 약간 다르다.</p>
 * <p>그 이유는 유저가 로그인을 한 상태로 서비스를 이용했다는 가정하에 테스트를 진행할 수 있어야 하기 때문이다.</p>
 * <p>시큐리티 테스트를 사용하면 테스트를 실행 전에 원하는 유저를 마치 로그인한 것 처럼 설정할 수 있다.</p>
 *
 * <ul>가짜 유저를 세팅하는 3가지 방법.
 *     <li>{@code @WithMockUser}
 *         <ul><li>특정 사용자가 존재하는 것처럼 테스트 진행할 수 있다.</li></ul>
 *     </li>
 *     <li>{@code @WithUserDetails}
 *         <ul><li>사용자를 가짜로 로그인할 수 있다.</li></ul>
 *     </li>
 *     <li>{@code ~.with}
 *         <ul><li>직접 사용자를 mockMvc에 지정하는 방식이다.</li></ul>
 *     </li>
 * </ul>
 */
@SpringBootTest
@ActiveProfiles(profiles = "test")
@Transactional
class NoteControllerTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private NoteRepository noteRepository;
    private MockMvc mockMvc;
    private User user;
    private User admin;

    @BeforeEach
    public void setUp(@Autowired WebApplicationContext applicationContext) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .alwaysDo(print())
                .build();
        user = userRepository.save(new User("user123", "user", "ROLE_USER"));
        admin = userRepository.save(new User("admin123", "admin", "ROLE_ADMIN"));
    }

    @Test
    void getNote_인증없음() throws Exception {
        mockMvc.perform(get("/note"))
                .andExpect(redirectedUrlPattern("**/login"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    // WithUserDetails 로 테스트 하는 방법
    @WithUserDetails(
            value = "user123", // userDetailsService를 통해 가져올 수 있는 유저
            userDetailsServiceBeanName = "userDetailsService", // UserDetailsService 구현체의 Bean
            setupBefore = TestExecutionEvent.TEST_EXECUTION // 테스트 실행 직전에 유저를 가져온다.
    )
    void getNote_인증있음() throws Exception {
        mockMvc.perform(
                        get("/note")
                ).andExpect(status().isOk())
                .andExpect(view().name("note/index"))
                .andDo(print());
    }

    @Test
    void postNote_인증없음() throws Exception {
        mockMvc.perform(
                        post("/note").with(csrf())
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("title", "제목")
                                .param("content", "내용")
                ).andExpect(redirectedUrlPattern("**/login"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithUserDetails(
            value = "admin123",
            userDetailsServiceBeanName = "userDetailsService",
            setupBefore = TestExecutionEvent.TEST_EXECUTION
    )
    void postNote_어드민인증있음() throws Exception {
        mockMvc.perform(
                post("/note").with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("title", "제목")
                        .param("content", "내용")
        ).andExpect(status().isForbidden()); // 접근 거부
    }

    @Test
    @WithUserDetails(
            value = "user123",
            userDetailsServiceBeanName = "userDetailsService",
            setupBefore = TestExecutionEvent.TEST_EXECUTION
    )
    void postNote_유저인증있음() throws Exception {
        mockMvc.perform(
                post("/note").with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("title", "제목")
                        .param("content", "내용")
        ).andExpect(redirectedUrl("note")).andExpect(status().is3xxRedirection());
    }

    @Test
    void deleteNote_인증없음() throws Exception {
        Note note = noteRepository.save(new Note("제목", "내용", user));
        mockMvc.perform(
                        delete("/note?id=" + note.getId()).with(csrf())
                ).andExpect(redirectedUrlPattern("**/login"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithUserDetails(
            value = "user123",
            userDetailsServiceBeanName = "userDetailsService",
            setupBefore = TestExecutionEvent.TEST_EXECUTION
    )
    void deleteNote_유저인증있음() throws Exception {
        Note note = noteRepository.save(new Note("제목", "내용", user));
        mockMvc.perform(
                delete("/note?id=" + note.getId()).with(csrf())
        ).andExpect(redirectedUrl("note")).andExpect(status().is3xxRedirection());
    }

    @Test
    @WithUserDetails(
            value = "admin123",
            userDetailsServiceBeanName = "userDetailsService",
            setupBefore = TestExecutionEvent.TEST_EXECUTION
    )
    void deleteNote_어드민인증있음() throws Exception {
        Note note = noteRepository.save(new Note("제목", "내용", user));
        mockMvc.perform(
                delete("/note?id=" + note.getId()).with(csrf()).with(user(admin))
        ).andExpect(status().isForbidden()); // 접근 거부
    }
}