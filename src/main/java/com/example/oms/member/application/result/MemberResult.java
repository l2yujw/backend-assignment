package com.example.oms.member.application.result;

import com.example.oms.member.domain.Member;
import com.example.oms.member.domain.MemberGrade;

public final class MemberResult {

    private MemberResult() {}

    public record Created(Long memberId) {}

    public record MemberInfo(Long id, String name, MemberGrade grade) {
        public static MemberInfo from(Member member) {
            return new MemberInfo(
                    member.getId(),
                    member.getName(),
                    member.getGrade()
            );
        }
    }
}
