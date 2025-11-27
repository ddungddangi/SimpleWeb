package net.datasa.ex1_db.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity                 // 이 클래스가 DB 테이블이 된다는 표시
@Table(name = "member") // DB 테이블 이름을 'member'로 지정
public class Member {
	
	@Id // 기본키(Primary Key) 설정
	private String memberId; // 아이디 (로그인할 때 사용)
	
	private String password; // 비밀번호
	
	private String name;     // 이름
	
	private Integer age;     // 나이 (int 대신 Integer 쓰면 null 처리 가능)
}