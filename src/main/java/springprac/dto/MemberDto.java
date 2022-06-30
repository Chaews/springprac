package springprac.dto;

import lombok.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import springprac.domain.MemberEntity;
import springprac.domain.Role;

@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor @ToString
public class MemberDto {
    private int mno;
    private String mid;
    private String mname;
    private String mpassword;
    private String memail;
    private String oauth;
    private Role role;

    public MemberEntity tomemberEntity(MemberDto memberDto){

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        return MemberEntity.builder()
                .mid(this.mid)
                .mpassword(passwordEncoder.encode(this.mpassword))
                .mname(this.mname)
                .memail(this.memail)
                .role(Role.MEMBER)
                .build();
    }
}
