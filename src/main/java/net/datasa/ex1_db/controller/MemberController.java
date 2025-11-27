package net.datasa.ex1_db.controller;

import net.datasa.ex1_db.domain.MemberDTO; // ★ Member 대신 MemberDTO 사용
import net.datasa.ex1_db.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import jakarta.servlet.http.HttpSession;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MemberController {
	
	private final MemberService memberService;
	
	// 홈 화면 이동
	@GetMapping("/")
	public String home() {
		return "home";
	}
	
	// 1. 가입 폼으로 이동 (GET)
	@GetMapping("join")
	public String joinForm() {
		return "joinForm";
	}
	
	// 2. 가입 처리 (POST) - MemberDTO 사용
	@PostMapping("join")
	public String join(@ModelAttribute MemberDTO member, Model model) { // ★ 타입 변경
		
		// ID 중복 검사
		if (memberService.isIdDuplicate(member.getMemberId())) {
			model.addAttribute("msg", "이미 사용 중인 아이디(이메일)입니다.");
			model.addAttribute("url", "/join");
			return "alert";
		}
		
		memberService.join(member); // DTO를 서비스로 넘김
		
		// 성공 시
		model.addAttribute("msg", "회원가입을 축하합니다!");
		model.addAttribute("url", "/");
		return "alert";
	}
	
	// 3. 로그인 폼으로 이동
	@GetMapping("login")
	public String loginForm() {
		return "loginForm";
	}
	
	// 4. 로그인 처리 (POST) - ID/PW는 문자열 그대로 받음 (DTO 불필요)
	@PostMapping("login")
	public String login(String memberId, String password,
						HttpSession session, Model model) {
		
		String result = memberService.checkLogin(memberId, password);
		
		if (result.equals("success")) {
			session.setAttribute("loginId", memberId);
			return "redirect:/";
		} else if (result.equals("id_fail")) {
			model.addAttribute("msg", "등록되지 않은 아이디입니다.");
			model.addAttribute("url", "/login");
			return "alert";
		} else {
			model.addAttribute("msg", "비밀번호가 일치하지 않습니다.");
			model.addAttribute("url", "/login");
			return "alert";
		}
	}
	
	// 5. 로그아웃 처리
	@GetMapping("logout")
	public String logout(HttpSession session) {
		session.removeAttribute("loginId");
		return "redirect:/";
	}
	
	// 6. 회원 목록 페이지 이동
	@GetMapping("list")
	public String list(Model model) {
		// ★ 서비스가 List<MemberDTO>를 리턴하므로 타입 변경
		List<MemberDTO> list = memberService.getList();
		
		model.addAttribute("memberList", list);
		log.debug("조회된 회원 수: {}", list.size());
		
		return "list";
	}
	
	// 7. 회원 정보 수정 폼 이동
	@GetMapping("update")
	public String updateForm(HttpSession session, Model model) {
		String loginId = (String) session.getAttribute("loginId");
		
		if (loginId == null) {
			return "redirect:/login";
		}
		
		// ★ 서비스에서 MemberDTO를 받아옴 (메서드명 getMember로 통일)
		MemberDTO member = memberService.getMember(loginId);
		
		model.addAttribute("member", member);
		return "updateForm";
	}
	
	// 8. 회원 정보 수정 처리
	@PostMapping("update")
	public String update(@ModelAttribute MemberDTO member) { // ★ 타입 변경
		log.debug("수정 요청 데이터: {}", member);
		memberService.update(member);
		return "redirect:/";
	}
}