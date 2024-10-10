package thespeace.practice.spring.security.notice;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * <h1>공지사항 Entity</h1>
 * <p>유저가 아닌 관리자만 작성, 때문에 User는 알 필요가 없다.</p>
 */
@Entity
@Table
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Notice {

    @Id
    @GeneratedValue
    private Long id;

    /**
     * <h2>공지사항 제목</h2>
     */
    private String title;

    /**
     * <h2>공지사항 내용</h2>
     */
    @Lob
    private String content;

    /**
     * <h2>등록일시</h2>
     */
    @CreatedDate
    private LocalDateTime createdAt;

    /**
     * <h2>수정일시</h2>
     */
    @LastModifiedDate
    private LocalDateTime updatedAt;

    public Notice(
            String title,
            String content
    ) {
        this.title = title;
        this.content = content;
    }
}