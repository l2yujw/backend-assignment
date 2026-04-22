package com.example.oms.member.application;

import com.example.oms.core.annotation.Reader;
import com.example.oms.core.error.DomainException;
import com.example.oms.member.domain.Member;
import com.example.oms.member.domain.error.MemberError;
import com.example.oms.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@Reader
@RequiredArgsConstructor
public class MemberReader {

    private final MemberRepository memberRepository;

    public Member getByIdOrThrow(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new DomainException(MemberError.MEMBER_NOT_FOUND, "memberId=" + id));
    }

    public void assertExists(Long id) {
        if (!memberRepository.existsById(id)) {
            throw new DomainException(MemberError.MEMBER_NOT_FOUND, "memberId=" + id);
        }
    }
}
