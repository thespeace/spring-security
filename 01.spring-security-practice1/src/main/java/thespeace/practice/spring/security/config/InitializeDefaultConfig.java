package thespeace.practice.spring.security.config;

import lombok.RequiredArgsConstructor;
import thespeace.practice.spring.security.note.NoteService;
import thespeace.practice.spring.security.notice.NoticeService;
import thespeace.practice.spring.security.user.User;
import thespeace.practice.spring.security.user.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * <h1>초기 상태 등록 Config</h1>
 * <p>h2 DB를 사용하기 때문에, 예제의 사용 편의성을 위해 User,Admin 자동 등록을 위해 해당 설정 추가</p>
 * <p>만약에 디스크 저장방식의 DB(mysql, oracle)를 사용한다면 해당 클래스는 삭제해도 된다.</p>
 */
@Configuration
@RequiredArgsConstructor
@Profile(value = "!test") // test 에서는 제외
public class InitializeDefaultConfig {

    private final UserService userService;
    private final NoteService noteService;
    private final NoticeService noticeService;

    /**
     * <h2>유저 등록, note 4개 등록</h2>
     */
    @Bean
    public void initializeDefaultUser() {
        User user = userService.signup("user", "user");
        noteService.saveNote(user, "테스트1", "테스트1입니다.");
        noteService.saveNote(user, "테스트2", "테스트2입니다.");
        noteService.saveNote(user, "테스트3", "테스트3입니다.");
        noteService.saveNote(user, "여름 여행계획", "여름 여행계획 작성중...");
    }

    /**
     * <h2>어드민 등록, 공지사항 2개 등록</h2>
     */
    @Bean
    public void initializeDefaultAdmin() {
        userService.signupAdmin("admin", "admin");
        noticeService.saveNotice("환영합니다.", "환영합니다 여러분");
        noticeService.saveNotice("노트 작성 방법 공지", "1. 회원가입\n2. 로그인\n3. 노트 작성\n4. 저장\n* 본인 외에는 게시글을 볼 수 없습니다.");
    }
}
