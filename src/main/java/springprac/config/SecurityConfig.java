package springprac.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import springprac.service.MemberService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter { // 웹 시큐리티 설정 관련 상속 클래스

    @Autowired
    MemberService memberService; // 회원 관련 서비스

    @Autowired
    private PasswordEncoder passwordEncoder(){return new BCryptPasswordEncoder();} // 비밀번호 암호화

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests() // 요청 권한 설정
                .antMatchers("/member/memberonly") // 권한 설정할 URL
                .hasRole("MEMBER") // 필요 권한
                .and()
                .formLogin() // 로그인 설정
                .loginPage("/member/login") // 사용할 로그인 페이지 URL
                .loginProcessingUrl("/member/logincontroller") // 로그인 처리할 URL
                .defaultSuccessUrl("/") // 성공시 이동할 URL
                .usernameParameter("mid") // 로그인에 사용할 아이디
                .passwordParameter("mpassword") // 로그인에 사용할 비밀번호
                .failureUrl("/error") // 로그인 실패시 이동할 URL
                .and()
                .logout() // 로그아웃 설정
                .logoutRequestMatcher(new AntPathRequestMatcher("/member/logout")) // 로그아웃 처리할 URL
                .logoutSuccessUrl("/") // 로그아웃 성공시 이동할 URL
                .invalidateHttpSession(true) // 로그아웃성공시 세션 삭제
                .and()
                .csrf() // 서버에게 요청할 수 있는 페이지 제한
                .ignoringAntMatchers("/member/logincontroller") // 요청제한 제외시킬 URL
                .ignoringAntMatchers("/member/signup")// 요청제한 제외시킬 URL
                .and()
                .exceptionHandling() // 오류페이지 발생시 시큐리티가 페이지 전환
                .accessDeniedPage("/error")// 오류페이지 발생시 이동할 URL
                .and()
                .oauth2Login() // OAUTH 로그인
                .userInfoEndpoint() // 유저 정보가 들어오는 위치
                .userService(memberService); // 해당 서비스 클래스로 유저 정보 받음
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception { // 인증관리 메소드
        auth.userDetailsService(memberService).passwordEncoder(passwordEncoder()); // 인증할 서비스 객체, 패스워드 인코딩
    }
}
