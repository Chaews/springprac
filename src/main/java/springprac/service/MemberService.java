package springprac.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springprac.domain.MemberEntity;
import springprac.domain.MemberRepository;
import springprac.dto.LoginDto;
import springprac.dto.MemberDto;
import springprac.dto.OauthDto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class MemberService implements UserDetailsService, OAuth2UserService<OAuth2UserRequest, OAuth2User> {
                                    // UserDetailsService -> 일반회원 loadUserByUsername 구현
                                    // OAuth2UserService -> Oauth 회원  OAuth2User 구현
    @Autowired // 자동 메모리 생성
    MemberRepository memberRepository ; // DAO 역할

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException { // Oauth 회원  OAuth2User 구현
        // OAuth2UserRequest : 인증 결과 호출 클래스

        // 인증 결과 정보 요청
        OAuth2UserService oAuth2UserService = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);

        // 클라이언트 아이디 [ 네이버 vs 카카오] : oauth 구분용 으로 사용할 변수
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        // 회원정보 요청시 사용되는 JSON 키 이름 호출  : 회원정보 호출시 사용되는 키 이름
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        // oauth2 정보 -> Dto
        OauthDto oauthDto = OauthDto.of(registrationId,userNameAttributeName, oAuth2User.getAttributes() );

        // 이메일 주소로 엔티티 찾기
        Optional<MemberEntity> optional =memberRepository.findBymemail(oauthDto.getMemail());
        if(!optional.isPresent()){ // 존재하지 않으면
            memberRepository.save(oauthDto.tomemberentity()); // DB에 저장
        }

        return new DefaultOAuth2User( // 반환타입 DefaultOAuth2User ( 권한(role)명 , 회원인증정보 , 회원정보 호출키 )
                // DefaultOAuth2User , UserDetails : 반환시 인증세션 자동 부여 [ SimpleGrantedAuthority : (권한) 필수 ]
                Collections.singleton(new SimpleGrantedAuthority("ROLE_MEMBER")),
                oAuth2User.getAttributes(),
                userNameAttributeName);
    }


    // * 로그인 서비스 제공 메소드
    // 1. 패스워드 검증 X [ 시큐리티 제공 ]
    // 2. 아이디만 검증 처리
    // 3. 권한 키 검증 처리
    @Override
    public UserDetails loadUserByUsername(String mid) throws UsernameNotFoundException {  // 일반회원 loadUserByUsername 구현
        // 아이디로 엔티티 찾기
        Optional<MemberEntity> optional = memberRepository.findBymid(mid);
        MemberEntity memberEntity = null ; // ispresent 조건문때문에 밖에 선언  .isElse랑 같음
        if(optional.isPresent()){ // 존재하면
            memberEntity = optional.get(); // optional에서 가져온다
        }
        // 찾은 회원엔티티의 권한을 리스트에 담기
        List<GrantedAuthority> authorityList = new ArrayList<>();  // GrantedAuthority 부여된 인증의 클래스
        authorityList.add(new SimpleGrantedAuthority(memberEntity.getrolekey()));  // 리스트에 인증된 엔티티의 키를 보관
        return new LoginDto(memberEntity, authorityList); // 회원엔티티 , 인증된 리스트를  인증세션 부여
    }

    @Transactional // 트랜잭션
    public boolean signup(MemberDto memberDto){ // DB 저장 메소드
        MemberEntity memberEntity = memberDto.tomemberEntity(memberDto); // DTO -> ENTITY
        memberRepository.save(memberEntity); // DB에 저장

        if(memberEntity.getMno() < 1){ // 저장되면 mno 1이상 생성되므로  저장안됐을경우를 말함
            return false; // false 반환
        }
        else {
            memberEntity.setOauth("local"); // 회원 TYPE 로컬로 저장
            return true; // true 반환
        }
    }



}
