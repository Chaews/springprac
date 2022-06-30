package springprac.domain;


import lombok.*;
import org.hibernate.annotations.GeneratorType;

import javax.persistence.*;

@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor @ToString
@Table(name="member") // 테이블 이름 정의
@Entity // 개체
public class MemberEntity {

    @Id // 기본키 설정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본키 생성
    private int mno; // 회원번호
    private String mid; // 아이디
    private String mname; // 이름
    private String mpassword; //비밀번호
    private String memail; // 이메일
    private String oauth; // 회원type

    @Enumerated(EnumType.STRING) // DB에 저장될 enum 타입 설정 String - 이름
    private Role role;

    public String getrolekey() { // role 중에서 key값 반환 메소드 // 시큐리티에서 인증허가 된 리스트에 보관하기 위해서
        return role.getKey();
    }


}
