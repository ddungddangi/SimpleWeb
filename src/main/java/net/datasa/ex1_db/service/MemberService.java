package net.datasa.ex1_db.service;

import lombok.RequiredArgsConstructor;
import net.datasa.ex1_db.domain.Member;
import net.datasa.ex1_db.domain.MemberDTO; // MemberDTO 임포트 필수
import net.datasa.ex1_db.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {
	
	private final MemberRepository memberRepository;
	
	// 1. 회원가입 (DTO를 받아서 -> Entity로 변환 후 저장)
	public void join(MemberDTO dto) {
		// DTO -> Entity 변환 (Builder 패턴 사용)
		Member member = Member.builder()
				.memberId(dto.getMemberId())
				.password(dto.getPassword())
				.name(dto.getName())
				.age(dto.getAge())
				.build();
		
		memberRepository.save(member);
	}
	
	// 2. 전체 회원 목록 조회 (Entity 리스트 -> DTO 리스트로 변환)
	public List<MemberDTO> getList() {
		List<Member> entityList = memberRepository.findAll();
		List<MemberDTO> dtoList = new ArrayList<>();
		
		for (Member entity : entityList) {
			// Entity -> DTO 변환
			MemberDTO dto = MemberDTO.builder()
					.memberId(entity.getMemberId())
					.password(entity.getPassword())
					.name(entity.getName())
					.age(entity.getAge())
					.build();
			dtoList.add(dto);
		}
		return dtoList;
	}
	
	// 3. 회원 정보 수정 (DTO를 받아서 처리)
	public void update(MemberDTO dto) {
		// DB에서 Entity 조회
		Member foundMember = memberRepository.findById(dto.getMemberId()).orElse(null);
		
		if (foundMember != null) {
			// Entity의 내용을 DTO 값으로 변경
			foundMember.setPassword(dto.getPassword());
			foundMember.setName(dto.getName());
			foundMember.setAge(dto.getAge());
			
			// JPA의 변경 감지(Dirty Checking)로 인해 트랜잭션 종료 시 자동 update 쿼리 날아감
			// 명시적으로 save를 호출해도 됩니다.
			memberRepository.save(foundMember);
		}
	}
	
	// 4. 내 정보 조회 (Controller 이름에 맞춰 select -> getMember로 변경 추천)
	// 리턴 타입: Member -> MemberDTO
	public MemberDTO getMember(String memberId) {
		Member member = memberRepository.findById(memberId).orElse(null);
		
		if (member == null) {
			return null;
		}
		
		// Entity -> DTO 변환 후 리턴
		return MemberDTO.builder()
				.memberId(member.getMemberId())
				.password(member.getPassword())
				.name(member.getName())
				.age(member.getAge())
				.build();
	}
	
	// 5. 아이디 중복 체크 (String은 DTO 변환 필요 없음)
	public boolean isIdDuplicate(String memberId) {
		return memberRepository.existsById(memberId);
	}
	
	// 6. 로그인 상세 검사 (String은 DTO 변환 필요 없음)
	public String checkLogin(String memberId, String password) {
		Member member = memberRepository.findById(memberId).orElse(null);
		
		if (member == null) {
			return "id_fail";
		}
		if (member.getPassword().equals(password)) {
			return "success";
		}
		return "pw_fail";
	}
}