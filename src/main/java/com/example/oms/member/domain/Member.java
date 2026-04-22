package com.example.oms.member.domain;

import com.example.oms.core.annotation.AggregateRoot;
import com.example.oms.core.guard.Guard;
import com.example.oms.core.persistence.AbstractTime;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@AggregateRoot
@Table(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends AbstractTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private MemberGrade grade;

    private Member(String name, MemberGrade grade) {
        this.name  = Guard.notBlank(name, "name");
        this.grade = Guard.notNull(grade, "grade");
    }

    public static Member create(String name, MemberGrade grade) {
        return new Member(name, grade);
    }
}
