package springprac.dto;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import springprac.domain.MemberEntity;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
public class LoginDto implements UserDetails { // 로그인 세션에 넣을 DTO
    private int mno; // 회원번호
    private String mid; // 아이디
    private String mname; // 이름
    private String mpassword; // 비밀번호
    private final Set<GrantedAuthority> authorities ; // 부여된 인증들의 권한

    public LoginDto(MemberEntity memberEntity, Collection<? extends GrantedAuthority> authorityList){ // 생성자
        this.mno = memberEntity.getMno();  // 인수로 받은 엔티티의 회원번호
        this.mid = memberEntity.getMid(); // 인수로 받은 엔티티의 아이디
        this.mname = memberEntity.getMname(); // 인수로 받은 엔티티의 회원이름
        this.mpassword = memberEntity.getMpassword(); // 인수로 받은 엔티티의 비밀번호
        this.authorities = Collections.unmodifiableSet(new LinkedHashSet<>(authorityList)); // 인수로 받은 엔티티의 권한

    }


    @Override // Userdetails 인터페이스 메소드 구현
    public String getPassword() {
        return this.mpassword;
    }

    @Override // Userdetails 인터페이스 메소드 구현
    public String getUsername() {
        return this.mid;
    }

    @Override // Userdetails 인터페이스 메소드 구현
    public boolean isAccountNonExpired() {
        return true;
    } // 계정 인증 만료 여부

    @Override // Userdetails 인터페이스 메소드 구현
    public boolean isAccountNonLocked() {
        return true;
    } // 계정 잠김 여부

    @Override // Userdetails 인터페이스 메소드 구현
    public boolean isCredentialsNonExpired() {
        return true;
    } // 계정 패스워드 만료 여부 확인

    @Override // Userdetails 인터페이스 메소드 구현
    public boolean isEnabled() {
        return true;
    } // 계정 사용 가능여부 확인
}
