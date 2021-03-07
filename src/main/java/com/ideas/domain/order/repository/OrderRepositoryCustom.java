package com.ideas.domain.order.repository;


import com.ideas.domain.order.Order;

public interface OrderRepositoryCustom {

    Order findByOrder(Long memberId, String orderNum);
}
