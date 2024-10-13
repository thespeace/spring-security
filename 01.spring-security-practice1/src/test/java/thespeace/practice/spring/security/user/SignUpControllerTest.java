package thespeace.practice.spring.security.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * <h1>MockMvc</h1>
 * <p>서버 배포의 동작을 재현, 직관적이라 이해하기가 쉽다.</p>
 * <ul>
 *     <li>{@code perform}
 *         <ul>
 *             <li>요청을 전송하는 역할을 한다. 결과로 ResultActions를 반환한다.</li>
 *             <li>get, post, put, delete : perform() 안에 넣어서 요청한 http method를 전한다. 인자로 경로를 적어준다.</li>
 *             <li>params : Key Value 파라미터를 전달할 수 있다. 여러 개일 때는 params, 한개면 param을 사용한다.</li>
 *         </ul>
 *     </li>
 *     <li>{@code andExpect}
 *         <ul>
 *             <li>응답을 검증한다.</li>
 *             <li>status() : 상태를 검증한다. ex) isOk(200), isNotFound(404)</li>
 *             <li>view() : 응답으로 받은 뷰 이름을 검증한다.</li>
 *             <li>redirect() : 응답으로 받은 redirect를 검증한다.</li>
 *             <li>content() : 응답 body를 검증한다.</li>
 *         </ul>
 *     </li>
 *     <li>{@code andDo}
 *         <ul>
 *             <li>일반적으로 해야할 일을 표현, andDo(print()) 하면 결과를 print한다.</li>
 *         </ul>
 *     </li>
 * </ul>
 */
@SpringBootTest
@Transactional
class SignUpControllerTest {

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp(@Autowired WebApplicationContext applicationContext) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .alwaysDo(print())
                .build();
    }

    @Test
    void signup() throws Exception {
        mockMvc.perform(
                post("/signup").with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("username", "user123")
                        .param("password", "password")
        ).andExpect(redirectedUrl("login")).andExpect(status().is3xxRedirection());
    }
}