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
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    MemberService memberService;

    @Autowired
    private PasswordEncoder passwordEncoder(){return new BCryptPasswordEncoder();}

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
                .ignoringAntMatchers("/member/logincontroller")
                .ignoringAntMatchers("/member/signup")
                .and()
                .exceptionHandling()
                .accessDeniedPage("/error")
                .and()
                .oauth2Login()
                .userInfoEndpoint()
                .userService(memberService);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(memberService).passwordEncoder(passwordEncoder());
    }
}
