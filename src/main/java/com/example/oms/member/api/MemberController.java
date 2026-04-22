package com.example.oms.member.api;

import com.example.oms.core.response.ApiResponse;
import com.example.oms.member.api.request.MemberRequest;
import com.example.oms.member.api.response.MemberResponse;
import com.example.oms.member.application.MemberService;
import com.example.oms.member.application.command.MemberCommand;
import com.example.oms.platform.web.response.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<ApiResponse<MemberResponse.Created>> register(
            @Valid @RequestBody MemberRequest.Register request
    ) {
        MemberResponse.Created result = MemberResponse.Created.from(
                memberService.register(new MemberCommand.Register(request.name(), request.grade()))
        );
        return ApiResponses.created(URI.create("/api/v1/members/" + result.memberId()), result);
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<ApiResponse<MemberResponse.MemberInfo>> getInfo(
            @PathVariable Long memberId
    ) {
        return ApiResponses.ok(MemberResponse.MemberInfo.from(memberService.getInfo(memberId)));
    }
}


