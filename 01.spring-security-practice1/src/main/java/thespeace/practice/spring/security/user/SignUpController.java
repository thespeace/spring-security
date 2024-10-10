package thespeace.practice.spring.security.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * <h1>회원가입 Controller</h1>
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/signup")
public class SignUpController {

    /**
     * @return 회원 가입 페이지 리소스
     */
    @GetMapping
    public String signup() {
        return "signup";
    }
}
