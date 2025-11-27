package net.datasa.ex1_db.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDTO {
	private String memberId;
	private String password;
	private String name;
	private Integer age;
	
	// Entity -> DTO 로 변환하는 생성자
	// DB에서 가져온 Member 객체를 이 DTO로 바꿀 때 씁니다.
	public static MemberDTO fromEntity(Member entity) {
		return MemberDTO.builder()
				.memberId(entity.getMemberId())
				.password(entity.getPassword())
				.name(entity.getName())
				.age(entity.getAge())
				.build();
	}
}