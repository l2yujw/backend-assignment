package com.example.oms.order.application.command;

public final class OrderCommand {

    private OrderCommand() {}

    public record Place(Long memberId, String productName, int originalPrice) {}
}
