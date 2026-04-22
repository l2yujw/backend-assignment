package com.example.oms.member.domain.repository;

import com.example.oms.member.domain.Member;

import java.util.Optional;

public interface MemberRepository {
    Member save(Member member);
    Optional<Member> findById(Long id);
    boolean existsById(Long id);
}
