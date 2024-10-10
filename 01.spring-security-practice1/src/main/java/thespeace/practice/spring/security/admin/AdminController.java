package thespeace.practice.spring.security.admin;

import lombok.RequiredArgsConstructor;
import thespeace.practice.spring.security.note.Note;
import thespeace.practice.spring.security.note.NoteService;
import thespeace.practice.spring.security.user.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final NoteService noteService;

    /**
     * <h2>관리자인 경우 노트 조회</h2>
     *
     * @return admin/index.html
     */
    @GetMapping
    public String getNoteForAdmin(Authentication authentication, Model model) {
        User user = (User) authentication.getPrincipal();
        List<Note> notes = noteService.findByUser(user);
        model.addAttribute("notes", notes);
        return "admin/index";
    }
}
