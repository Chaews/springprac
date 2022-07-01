package springprac.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import springprac.dto.MemberDto;
import springprac.service.MemberService;

@Controller // 템플릿반환 컨트롤러 클래스
public class IndexController {

    @Autowired // 생성자를 자동으로 주입
    MemberService memberService; // 멤버서비스 클래스

    @GetMapping("/") // 최상위 경로 매핑
    public String main(){  // main.html 반환
        return "main";
    }
    @GetMapping("/member/login") // 로그인 경로 매핑
    public String login(){ // 로그인 페이지 반환
        return "login";
    }

    @GetMapping("/member/memberonly") // 경로 매핑
    public String memberonly(){ // memberonly 페이지 반환
        return "memberonly";
    }

    @GetMapping("/member/signup") // 경로 매핑
    public String signup(){ // signup 페이지 반환
        return "signup";
    }

    @PostMapping("/member/signup") // POST요청 경로 매핑
    public String signup(MemberDto memberDto){
        boolean result = memberService.signup(memberDto); // 회원가입 처리 후 결과 반환
        if(result){
            return "main"; // 성공시 main.html 반환
        }
        else{
            return "error"; // 실패시 error.html 반환
        }
    }
}
