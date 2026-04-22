package com.example.oms.member.application.command;

public final class MemberCommand {

    private MemberCommand() {}

    public record Register(String name, String grade) {}
}
