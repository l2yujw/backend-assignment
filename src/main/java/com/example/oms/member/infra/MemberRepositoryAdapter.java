package com.example.oms.member.infra;

import com.example.oms.member.domain.Member;
import com.example.oms.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryAdapter implements MemberRepository {

    private final JpaMemberRepository jpa;

    @Override
    public Member save(Member member) {
        return jpa.save(member);
    }

    @Override
    public Optional<Member> findById(Long id) {
        return jpa.findById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return jpa.existsById(id);
    }
}
