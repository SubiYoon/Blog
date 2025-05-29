package com.devstat.blog.core.code;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum StatusCode {
    /**
     * 공통코드
     */
    SUCCESS(HttpStatus.OK, "SUCCESS", "성공"),
    PAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "PAGE_NOT_FOUND", "페이지를 찾을 수 없습니다."),

    /**
     * 회원
     */
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_NOT_FOUND", "해당 유저가 존재하지 않습니다."),
    TOKEN_NOT_VALID(HttpStatus.UNAUTHORIZED, "TOKEN_NOT_VALID", "토큰이 유효하지 않습니다.\n 다시 로그인해 주세요."),
    TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "TOKEN_NOT_FOUND", "토큰이 존재하지 않습니다.\n 로그인해 주세요."),
    DUPLICATED_ID(HttpStatus.BAD_REQUEST, "DUPLICATED_ID", "중복된 아이디가 존재합니다."),
    IS_NOT_ADMIN_USER(HttpStatus.FORBIDDEN, "IS_NOT_ADMIN_USER", "관리자 회원이 시도한 요청이 아닙니다."),

    /**
     * Menu
     */
    MENU_NOT_FOUND(HttpStatus.NOT_FOUND, "MENU_NOT_FOUND", "해당 메뉴를 찾을 수 없습니다.");

    /**
     * 커스텀해서 사용할 변수들
     */
    private final HttpStatus statusCode;
    private final String customCode;
    private final String massage;

    private StatusCode(HttpStatus statusCode, String customCode, String massage) {
        this.statusCode = statusCode;
        this.customCode = customCode;
        this.massage = massage;
    }
}
