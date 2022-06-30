package springprac.domain;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter @RequiredArgsConstructor
public enum Role {

    MEMBER("ROLE_MEMBER","일반회원"),
    ADMIN("ROLE_ADMIN","관리자");


    private final String key;
    private final String value;

}
