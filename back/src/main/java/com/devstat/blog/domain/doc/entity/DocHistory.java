package com.devstat.blog.domain.doc.entity;

import com.devstat.blog.core.baseEntity.BaseEntity;
import com.devstat.blog.core.code.FileStatusCode;
import com.devstat.blog.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DocHistory extends BaseEntity {

    @Id
    @Column(name = "doc_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "doc_seq_generator")
    @SequenceGenerator(name = "doc_seq_generator", sequenceName = "doc_seq", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_alias")
    private Member member;

    @Column(name = "file_path", nullable = false)
    private String filePath;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private FileStatusCode status;

    public DocHistory(Member member, String filePath, FileStatusCode status) {
        this.member = member;
        this.filePath = filePath;
        this.status = status;
    }

    public static DocHistory of(Member member, String filePath, FileStatusCode status) {
        return new DocHistory(member, filePath, status);
    }
}
