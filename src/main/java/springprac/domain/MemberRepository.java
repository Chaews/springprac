package springprac.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity, Integer> { // DAO역할

    Optional<MemberEntity> findBymid(String mid); // 아이디를 이용하여 엔티티 검색

    Optional<MemberEntity> findBymemail(String memail); // 이메일을 이용하여 엔티티 검색
}
