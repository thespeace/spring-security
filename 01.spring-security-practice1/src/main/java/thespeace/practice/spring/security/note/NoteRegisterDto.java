package thespeace.practice.spring.security.note;

import lombok.Getter;
import lombok.Setter;

/**
 * <h1>노트 등록 Dto</h1>
 */
@Getter
@Setter
public class NoteRegisterDto {

    private String title;
    private String content;
}