package com.ideas.domain.order.application;

import com.ideas.domain.member.dto.MemberOrderDto;
import com.ideas.domain.order.dto.OrderDto;
import com.ideas.domain.order.dto.OrderParamDto;

public interface OrderApplication {
    /**
     * 회원 주문 추가
     *
     * @param memberId
     * @param orderParamDto
     * @return
     */
    OrderDto createOrder(Long memberId, OrderParamDto orderParamDto);


    /**
     * 회원의 주문 정보를 조회
     *
     * @param memberId
     * @param orderNum
     * @return
     */
    MemberOrderDto findByMemberOrder(Long memberId, String orderNum);

    /**
     * 회원 주문 정보 수정
     *
     * @param memberId
     * @param orderParamDto
     * @return
     */
    OrderDto updateOrder(Long memberId, OrderParamDto orderParamDto);
}