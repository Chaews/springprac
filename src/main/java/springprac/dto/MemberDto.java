package springprac.dto;

import lombok.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import springprac.domain.MemberEntity;
import springprac.domain.Role;

@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor @ToString
public class MemberDto {
    private int mno; // 회원번호
    private String mid; // 아이디
    private String mname; // 이름
    private String mpassword; // 비밀번호
    private String memail; // 이메일
    private String oauth; // 회원타입
    private Role role; // 권한

    public MemberEntity tomemberEntity(MemberDto memberDto){ // DTO -> ENTITY 변환 메소드

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(); // 패스워드 암호화를 위해 객체 선언

        return MemberEntity.builder() // builder를 사용하여 entity 생성-> 반환
                .mid(this.mid)
                .mpassword(passwordEncoder.encode(this.mpassword)) // 암호화된 패스워드 삽입
                .mname(this.mname)
                .memail(this.memail)
                .role(Role.MEMBER) // 권한 삽입
                .build();
    }
}
