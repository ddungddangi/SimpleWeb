package net.datasa.ex1_db.repository;

import net.datasa.ex1_db.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// <다루는 엔티티 클래스, PK의 타입>
@Repository
public interface MemberRepository extends JpaRepository<Member, String> {
	// ⚠️ 내부에 아무것도 안 적는 것이 정답입니다!
	// JpaRepository가 findById, save, delete 등을 자동으로 다 만들어줍니다.
}