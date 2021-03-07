package com.ideas.domain.member.dto;


import com.ideas.domain.order.OrderStatusEnum;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Data
@NoArgsConstructor
public class MemberOrderDto {
    private Long memberId;
    private String name;
    private String nickname;
    private String phone;
    private String email;
    private LocalDateTime createDt;
    private LocalDateTime updateDt;
    private Long orderId;
    private String orderNum;
    private String productName;
    private OrderStatusEnum orderStatus;
    private LocalDateTime paymentDt;

    @QueryProjection
    public MemberOrderDto(Long memberId, String name, String nickname, String phone, String email, LocalDateTime createDt, LocalDateTime updateDt, Long orderId, String orderNum, String productName, OrderStatusEnum orderStatus, LocalDateTime paymentDt) {
        this.memberId = memberId;
        this.name = name;
        this.nickname = nickname;
        this.phone = phone;
        this.email = email;
        this.createDt = createDt;
        this.updateDt = updateDt;
        this.orderId = orderId;
        this.orderNum = orderNum;
        this.productName = productName;
        this.orderStatus = orderStatus;
        this.paymentDt = paymentDt;
    }
}
