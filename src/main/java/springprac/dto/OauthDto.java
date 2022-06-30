package springprac.dto;

import lombok.*;
import springprac.domain.MemberEntity;
import springprac.domain.Role;

import java.util.Map;

@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor @ToString
public class OauthDto { // oauth 회원정보 담고 있는 DTO
    private String mid; // 아이디
    private String mname; // 이름
    private String memail; // 이메일
    private String nameAttributeKey; // 회원정보 요청 키
    private Map<String, Object> attributes; // 인증 결과 (JSON)
    private String registartionId; // 클라이언트 ID

    // 클라이언트 구분 메소드
    public static OauthDto of(String registartionId, String nameAttributekey, Map<String, Object> attributes) {
        if(registartionId.equals("naver")){ // 클라이언트 ID가 네이버 일경우
            return ofnaver(registartionId, nameAttributekey , attributes);
        }
        else if(registartionId.equals("kakao")){ // 클라이언트 ID가 카카오 일경우
            return ofkakao(registartionId, nameAttributekey , attributes);
        }
        return null; // 없으면 null
    }


    // registartionId가 네이버면
    public static OauthDto ofnaver(String registartionId, String nameAttributekey, Map<String, Object> attributes){
        Map<String, Object> response =(Map<String, Object>) attributes.get(nameAttributekey); // 반환타입 JSON -> MAP 으로 받음
        String mid = ( (String)response.get("email") ).split("@")[0]; // 이메일주소에서 아이디만 추출
        return OauthDto.builder() // oauth dto 생성 -> 반환
                .mid(mid) // 아이디
                .mname((String)response.get("name")) // 요청 변수 이름은 네이버개발자 문서 참고
                .memail((String)response.get("email")) // 요청 변수 이름은 네이버개발자 문서 참고
                .registartionId(registartionId) // 클라이언트 ID
                .attributes(attributes) // 인증 결과 (JSON)
                .nameAttributeKey(nameAttributekey)   // 회원정보 요청 키
                .build();
    }
    public static OauthDto ofkakao(String registartionId, String nameAttributekey, Map<String, Object> attributes){
        Map<String, Object> kakao_account =(Map<String, Object>) attributes.get(nameAttributekey); // 반환타입 JSON -> MAP 으로 받음
        Map<String, Object> profile =(Map<String, Object>) kakao_account.get("profile"); // 반환타입 JSON -> MAP 으로 받음
        String mid = ( (String)kakao_account.get("email") ).split("@")[0]; // 이메일주소에서 아이디만 추출
        return OauthDto.builder()  // oauth dto 생성 -> 반환
                .mid(mid) // 아이디
                .mname((String)profile.get("nickname")) // 요청 변수 이름은 카카오개발자 문서 참고
                .memail((String)kakao_account.get("email")) // 요청 변수 이름은 카카오개발자 문서 참고
                .registartionId(registartionId) // 클라이언트 ID
                .attributes(attributes) // 인증 결과 (JSON)
                .nameAttributeKey(nameAttributekey) // 회원정보 요청 키
                .build();
    }


    public MemberEntity tomemberentity() { // oauth 정보-> 엔티티로 변경
        return MemberEntity.builder()
                .mid(this.mid) // 아이디
                .mname(this.mname) // 이름
                .memail(this.memail) // 이메일주소
                .oauth(this.registartionId) // 클라이언트 ID
                .role(Role.MEMBER) // 권한
                .build();
    }
}
