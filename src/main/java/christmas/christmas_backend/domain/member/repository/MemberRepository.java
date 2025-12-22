package christmas.christmas_backend.domain.member.repository;

import christmas.christmas_backend.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByStudentId(String studentId);

    boolean existsByStudentId(String studentId);
}

