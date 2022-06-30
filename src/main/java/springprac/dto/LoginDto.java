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
public class LoginDto implements UserDetails {
    private int mno;
    private String mid;
    private String mname;
    private String mpassword;
    private final Set<GrantedAuthority> authorities ;

    public LoginDto(MemberEntity memberEntity, Collection<? extends GrantedAuthority> authorityList){
        this.mno = memberEntity.getMno();
        this.mid = memberEntity.getMid();
        this.mname = memberEntity.getMname();
        this.mpassword = memberEntity.getMpassword();
        this.authorities = Collections.unmodifiableSet(new LinkedHashSet<>(authorityList));

    }


    @Override
    public String getPassword() {
        return this.mpassword;
    }

    @Override
    public String getUsername() {
        return this.mid;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
