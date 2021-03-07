package com.ideas.domain.order.application;

import com.ideas.domain.member.Member;
import com.ideas.domain.member.dto.MemberOrderDto;
import com.ideas.domain.member.repository.MemberRepository;
import com.ideas.domain.order.Order;
import com.ideas.domain.order.dto.OrderDto;
import com.ideas.domain.order.dto.OrderParamDto;
import com.ideas.domain.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OrderApplicationImpl implements OrderApplication {
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;

    @Override
    public OrderDto createOrder(final Long memberId, final OrderParamDto orderParamDto) {
        Optional<Member> optionalMember = memberRepository.findById(memberId);
        Member member = optionalMember.get();
        Order order = Order.createOrder(member, orderParamDto.getOrderNum(), orderParamDto.getProductName(), orderParamDto.getProductCount());
        order = orderRepository.save(order);

        return OrderDto.createOrderDto(member.getMemberId(), order);
    }

    @Override
    public MemberOrderDto findByMemberOrder(final Long memberId, final String orderNum) {
        return memberRepository.findByMemberOrder(memberId, orderNum);
    }

    @Override
    public OrderDto updateOrder(final Long memberId, final OrderParamDto orderParamDto) {
        MemberOrderDto memberOrderDto = memberRepository.findByMemberOrder(memberId, orderParamDto.getOrderNum());
        Optional<Order> optionalOrder = orderRepository.findById(memberOrderDto.getOrderId());
        Order order = optionalOrder.get();
        order.updateOrder(orderParamDto);
        orderRepository.save(order);

        return OrderDto.createOrderDto(memberId, order);
    }
}
