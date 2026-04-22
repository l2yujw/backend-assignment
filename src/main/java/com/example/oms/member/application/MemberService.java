package com.example.oms.member.application;

import com.example.oms.member.application.command.MemberCommand;
import com.example.oms.member.application.result.MemberResult;
import com.example.oms.member.domain.Member;
import com.example.oms.core.type.MemberGrade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberReader memberReader;
    private final MemberWriter memberWriter;

    @Transactional
    public MemberResult.Created register(MemberCommand.Register command) {
        MemberGrade grade = MemberGrade.valueOf(command.grade().toUpperCase());
        Member member = Member.create(command.name(), grade);
        memberWriter.create(member);
        return new MemberResult.Created(member.getId());
    }

    @Transactional(readOnly = true)
    public MemberResult.MemberInfo getInfo(Long memberId) {
        Member member = memberReader.getByIdOrThrow(memberId);
        return MemberResult.MemberInfo.from(member);
    }

    @Transactional(readOnly = true)
    public void assertExists(Long memberId) {
        memberReader.assertExists(memberId);
    }
}
