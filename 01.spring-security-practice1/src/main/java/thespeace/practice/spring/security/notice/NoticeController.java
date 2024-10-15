package thespeace.practice.spring.security.notice;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import thespeace.practice.spring.security.note.NoteRegisterDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <h1>공지사항 서비스 Controller</h1>
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/notice")
public class NoticeController {

    private final NoticeService noticeService;

    /**
     * <h2>공지사항 조회</h2>
     *
     * @return notice/index.html
     */
    @GetMapping
    public String getNotice(Model model) {
        SecurityContext securityContext = SecurityContextHolder.getContext(); //SecurityContext 내용 확인.
        List<Notice> notices = noticeService.findAll();
        model.addAttribute("notices", notices);
        return "notice/index";
    }

    /**
     * <h2>공지사항 등록</h2>
     *
     * @param noteDto 노트 등록 Dto
     * @return notice/index.html refresh
     */
    @PostMapping
    public String postNotice(@ModelAttribute NoteRegisterDto noteDto) {
        noticeService.saveNotice(noteDto.getTitle(), noteDto.getContent());
        return "redirect:notice";
    }

    /**
     * <h2>공지사항 삭제</h2>
     *
     * @param id 공지사항 ID
     * @return notice/index.html refresh
     */
    @DeleteMapping
    public String deleteNotice(@RequestParam Long id) {
        noticeService.deleteNotice(id);
        return "redirect:notice";
    }
}
