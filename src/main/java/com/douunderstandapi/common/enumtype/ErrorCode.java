package com.douunderstandapi.common.enumtype;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    //400
    NOT_EXIST_USER_EMAIL(4000, "유저 이메일이 존재하지 않습니다."),
    NOT_EXIST_KNOWLEDGE_ID(4001, "지식 아이디가 존재하지 않습니다."),
    ENCRYPTION_FAILED(4002, "암호화에 실패하였습니다."),
    DECRYPTION_FAILED(4003, "복호화에 실패하였습니다."),
    CRYPTOGRAPHY_FAILED(4004, "암복호화에 실패하였습니다"),
    NOT_EXIST_AUTH_CODE(4005, "해당 이메일의 인증코드가 존재하지 않습니다."),
    INVALID_AUTH_CODE(4006, "유효하지 않은 인증코드 입니다."),

    //500
    UNKNOWN(5000, "서버 내부 에러가 발생했습니다.");

    private final int code;
    private final String msg;
}
