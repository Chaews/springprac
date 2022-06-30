package springprac.dto;

import lombok.*;
import springprac.domain.MemberEntity;
import springprac.domain.Role;

import java.util.Map;

@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor @ToString
public class OauthDto {
    private String mid;
    private String mname;
    private String memail;
    private String nameAttributeKey;
    private Map<String, Object> attributes;
    private String registartionId;

    public static OauthDto of(String registartionId, String nameAttributekey, Map<String, Object> attributes) {
        if(registartionId.equals("naver")){
            return ofnaver(registartionId, nameAttributekey , attributes);
        }
        else if(registartionId.equals("kakao")){
            return ofkakao(registartionId, nameAttributekey , attributes);
        }
        return null;
    }

    public static OauthDto ofnaver(String registartionId, String nameAttributekey, Map<String, Object> attributes){
        Map<String, Object> response =(Map<String, Object>) attributes.get(nameAttributekey);
        String mid = ( (String)response.get("email") ).split("@")[0];
        return OauthDto.builder()
                .mid(mid)
                .mname((String)response.get("name"))
                .memail((String)response.get("email"))
                .registartionId(registartionId)
                .attributes(attributes)
                .nameAttributeKey(nameAttributekey)
                .build();
    }
    public static OauthDto ofkakao(String registartionId, String nameAttributekey, Map<String, Object> attributes){
        Map<String, Object> kakao_account =(Map<String, Object>) attributes.get(nameAttributekey);
        Map<String, Object> profile =(Map<String, Object>) kakao_account.get("profile");
        String mid = ( (String)kakao_account.get("email") ).split("@")[0];
        return OauthDto.builder()
                .mid(mid)
                .mname((String)profile.get("nickname"))
                .memail((String)kakao_account.get("email"))
                .registartionId(registartionId)
                .attributes(attributes)
                .nameAttributeKey(nameAttributekey)
                .build();
    }


    public MemberEntity tomemberentity() {
        return MemberEntity.builder()
                .mid(this.mid)
                .mname(this.mname)
                .memail(this.memail)
                .oauth(this.registartionId)
                .role(Role.MEMBER)
                .build();
    }
}
