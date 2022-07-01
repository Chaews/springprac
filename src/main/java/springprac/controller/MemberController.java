package springprac.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import springprac.service.MemberService;

@RestController // 템플릿이 아닌 객체를 반환하는 컨트롤러
public class MemberController {
    @Autowired // 생성자를 자동으로 주입
    MemberService memberService;

    @GetMapping("/member/info")
    public String memberinfo(){return memberService.인증결과호출();}
}
