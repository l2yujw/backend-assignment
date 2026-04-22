package com.example.oms.member.api.response;

import com.example.oms.member.application.result.MemberResult;

public final class MemberResponse {

    private MemberResponse() {}

    public record Created(Long memberId) {
        public static Created from(MemberResult.Created result) {
            return new Created(result.memberId());
        }
    }

    public record MemberInfo(Long id, String name, String grade) {
        public static MemberInfo from(MemberResult.MemberInfo result) {
            return new MemberInfo(
                    result.id(),
                    result.name(),
                    result.grade().name()
            );
        }
    }
}
