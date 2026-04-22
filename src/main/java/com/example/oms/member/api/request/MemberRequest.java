package com.example.oms.member.api.request;

import jakarta.validation.constraints.NotBlank;

public final class MemberRequest {

    private MemberRequest() {}

    public record Register(
            @NotBlank String name,
            @NotBlank String grade
    ) {}
}
