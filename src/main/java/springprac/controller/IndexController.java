package springprac.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import springprac.dto.MemberDto;
import springprac.service.MemberService;

@Controller
public class IndexController {

    @Autowired
    MemberService memberService;

    @GetMapping("/")
    public String main(){
        return "main";
    }
    @GetMapping("/member/login")
    public String login(){
        return "login";
    }

    @GetMapping("/member/memberonly")
    public String memberonly(){
        return "memberonly";
    }

    @GetMapping("/member/signup")
    public String signup(){
        return "signup";
    }

    @PostMapping("/member/signup")
    public String signup(MemberDto memberDto){
        boolean result = memberService.signup(memberDto);
        if(result){
            return "main";
        }
        else{
            return "error";
        }
    }
}
