package com.devstat.blog.core.code;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;

@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum StatusCode {
    /**
     * 공통코드
     */
    SUCCESS(HttpStatus.OK, "SUCCESS", "성공"),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER_NOT_FOUND", "회원을 찾을 수 없습니다."),
    PAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "PAGE_NOT_FOUND", "페이지를 찾을 수 없습니다."),
    DOCS_RESTART_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "DOCS_RESTART_FAIL", "DOCUSAURUS를 재실행하는데 실패했습니다."),
    GIT_TASK_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "GIT_TASK_FAIL", "GIT 테스크를 실행하는데 실패했습니다."),
    EXECUTE_FRONT_RESTART(HttpStatus.OK, "EXECUTE_FRONT_RESTART", "FRONT-END를 재시작하겠습니다."),

    /**
     * 회원
     */
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_NOT_FOUND", "해당 유저가 존재하지 않습니다."),
    TOKEN_NOT_VALID(HttpStatus.UNAUTHORIZED, "TOKEN_NOT_VALID", "토큰이 유효하지 않습니다.\n 다시 로그인해 주세요."),
    TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "TOKEN_NOT_FOUND", "토큰이 존재하지 않습니다.\n 로그인해 주세요."),
    DUPLICATED_ID(HttpStatus.BAD_REQUEST, "DUPLICATED_ID", "중복된 아이디가 존재합니다."),
    IS_NOT_ADMIN_USER(HttpStatus.FORBIDDEN, "IS_NOT_ADMIN_USER", "관리자 회원이 시도한 요청이 아닙니다."),

    /**
     * Doc
     */
    DOC_SELECT_FAIL(HttpStatus.NOT_FOUND, "DOC_SELECT_FAIL", "해당 문서를 찾을 수 없습니다."),
    INCLUDE_NOT_FILE_AND_FOLDER(HttpStatus.BAD_REQUEST, "INCLUDE_NOT_FILE_AND_FOLDER", "필터에는 파일과 폴더만 사용할 수 있습니다."),
    DOC_SAVE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "DOC_SAVE_FAIL", "문서 저장에 실패했습니다."),
    DOC_DELETE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "DOC_DELETE_FAIL", "문서 삭제에 실패했습니다."),

    /**
     * Portfolio
     */
    LOGO_EMPTY(HttpStatus.BAD_REQUEST, "LOGO_EMPTY", "로고를 찾을 수 없습니다."),
    LOGO_SAVE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "LOGO_SAVE_FAIL", "로고 저장에 실패했습니다."),
    COMPANY_NOT_FOUND(HttpStatus.NO_CONTENT, "COMPANY_NOT_FOUND", "회사 정보를 찾을 수 없습니다."),
    ITEM_NOT_FOUND(HttpStatus.NO_CONTENT, "ITEM_NOT_FOUND", "아이템 정보를 찾을 수 없습니다."),
    IMAGE_SAVE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "IMAGE_SAVE_FAIL", "이미지 저장에 실패했습니다."),
    PROJECT_NOT_FOUND(HttpStatus.NO_CONTENT, "PROJECT_NOT_FOUND", "프로젝트 정보를 찾을 수 없습니다."),;

    /**
     * 커스텀해서 사용할 변수들
     */
    private final HttpStatus statusCode;
    private final String customCode;
    private final String message;

    private StatusCode(HttpStatus statusCode, String customCode, String message) {
        this.statusCode = statusCode;
        this.customCode = customCode;
        this.message = message;
    }
}
