package com.ideas.domain.order.repository;


import com.ideas.domain.order.Order;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;


@Repository
@RequiredArgsConstructor
public class OrderRepositoryCustomImpl implements OrderRepositoryCustom {

    private final JPAQueryFactory query;


    @Override
    public Order findByOrder(Long memberId, String orderNum) {

        return null;
    }
}
