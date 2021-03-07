package com.ideas.domain.order.service;


import com.ideas.domain.member.dto.MemberOrderDto;
import com.ideas.domain.order.application.OrderApplicationImpl;
import com.ideas.domain.order.dto.OrderDto;
import com.ideas.domain.order.dto.OrderParamDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderApplicationImpl orderApplication;

    @Override
    @Transactional
    public OrderDto createOrder(final Long memberId, final OrderParamDto orderParamDto) {
        return orderApplication.createOrder(memberId, orderParamDto);
    }


    @Override
    public MemberOrderDto findByMemberOrder(final Long memberId, final String orderNum) {
        return orderApplication.findByMemberOrder(memberId, orderNum);
    }

    @Override
    @Transactional
    public OrderDto updateOrder(final Long memberId, final OrderParamDto orderParamDto) {
        return orderApplication.updateOrder(memberId, orderParamDto);
    }
}
