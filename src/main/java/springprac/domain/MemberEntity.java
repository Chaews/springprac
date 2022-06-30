package springprac.domain;


import lombok.*;
import org.hibernate.annotations.GeneratorType;

import javax.persistence.*;

@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor @ToString
@Table(name="member")
@Entity
public class MemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int mno;
    private String mid;
    private String mname;
    private String mpassword;
    private String memail;
    private String oauth;

    @Enumerated(EnumType.STRING)
    private Role role;

    public String getrolekey() {
        return role.getKey();
    }


}
