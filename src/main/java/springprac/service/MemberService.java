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

    @Autowired
    MemberRepository memberRepository ;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService oAuth2UserService = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        OauthDto oauthDto = OauthDto.of(registrationId,userNameAttributeName, oAuth2User.getAttributes() );

        Optional<MemberEntity> optional =memberRepository.findBymemail(oauthDto.getMemail());
        if(!optional.isPresent()){
            memberRepository.save(oauthDto.tomemberentity());
        }

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_MEMBER")),
                oAuth2User.getAttributes(),
                userNameAttributeName);
    }

    @Override
    public UserDetails loadUserByUsername(String mid) throws UsernameNotFoundException {
        Optional<MemberEntity> optional = memberRepository.findBymid(mid);
        MemberEntity memberEntity = null ;
        if(optional.isPresent()){
            memberEntity = optional.get();
        }
        List<GrantedAuthority> authorityList = new ArrayList<>();
        authorityList.add(new SimpleGrantedAuthority(memberEntity.getrolekey()));

        return new LoginDto(memberEntity, authorityList);
    }

    @Transactional
    public boolean signup(MemberDto memberDto){
        MemberEntity memberEntity = memberDto.tomemberEntity(memberDto);
        memberRepository.save(memberEntity);

        if(memberEntity.getMno() < 1){
            return false;
        }
        else {
            memberEntity.setOauth("local");
            return true;
        }
    }



}
