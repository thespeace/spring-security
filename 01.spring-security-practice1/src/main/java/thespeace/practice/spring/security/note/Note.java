package thespeace.practice.spring.security.note;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import thespeace.practice.spring.security.user.User;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Note {

    @Id
    @GeneratedValue
    private Long id;

    /**
     * <h2>제목</h2>
     */
    private String title;

    /**
     * <h2>내용</h2>
     */
    @Lob
    private String content;

    /**
     * <h2>User 참조</h2>
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @CreatedDate
    private LocalDateTime createdAt; // 생성일시
    @LastModifiedDate
    private LocalDateTime updatedAt; // 수정 일시

    public Note(
            String title,
            String content,
            User user
    ) {
        this.title = title;
        this.content = content;
        this.user = user;
    }

    public void updateContent(
            String title,
            String content
    ) {
        this.title = title;
        this.content = content;
    }
}