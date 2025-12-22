package christmas.christmas_backend.domain.member.entity;

import christmas.christmas_backend.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "members")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String studentId;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 100)
    private String major;

    @Column(nullable = false, length = 50)
    private String grade;

    @Column(nullable = false)
    private String completedSemesters;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MemberStatus status;

    @Builder
    public Member(String studentId, String name, String major, String grade, String completedSemesters) {
        this.studentId = studentId;
        this.name = name;
        this.major = major;
        this.grade = grade;
        this.completedSemesters = completedSemesters != null ? completedSemesters : "0";
        this.status = MemberStatus.ACTIVE;
    }

    public void updateInfo(String name, String major, String grade, String completedSemesters) {
        this.name = name;
        this.major = major;
        this.grade = grade;
        if (completedSemesters != null) {
            this.completedSemesters = completedSemesters;
        }
    }

    public void updateStatus(MemberStatus status) {
        this.status = status;
    }
}

