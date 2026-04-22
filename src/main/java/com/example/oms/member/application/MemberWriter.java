package com.example.oms.member.application;

import com.example.oms.core.annotation.Writer;
import com.example.oms.member.domain.Member;
import com.example.oms.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@Writer
@RequiredArgsConstructor
public class MemberWriter {

    private final MemberRepository memberRepository;

    public Member create(Member member) {
        return memberRepository.save(member);
    }
}
