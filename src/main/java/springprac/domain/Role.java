package springprac.domain;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter @RequiredArgsConstructor // final 이 붙거나 notnull 이 붙은 필드의 생성자를 자동 생성해주는 어노테이션
public enum Role { // 자바 자료형 - 열거형 [ 서로 연관된 필드들의 집합 구성 ]

    MEMBER("ROLE_MEMBER","일반회원"),// 열거형 MEMBER[0], ADMIN[1]
    ADMIN("ROLE_ADMIN","관리자");

    // 열거형 들어가는 필드 항목들
    private final String key; // final [상수 ] : 데이터 고정
    private final String value; // final [상수 ] : 데이터 고정

}
