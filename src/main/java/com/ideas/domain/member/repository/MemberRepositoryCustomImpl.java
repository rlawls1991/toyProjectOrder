package com.ideas.domain.member.repository;


import com.ideas.domain.member.dto.*;
import com.ideas.domain.order.OrderStatusEnum;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import static com.ideas.domain.member.QMember.member;
import static com.ideas.domain.order.QOrder.order;


@Repository
@RequiredArgsConstructor
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory query;

    @Override
    public MemberDto findByMember(final Long memberId) {
        return query
                .select(new QMemberDto(
                        member.memberId,
                        member.name,
                        member.phone,
                        member.email,
                        member.nickname,
                        member.createDt,
                        member.updateDt))
                .from(member)
                .where(memberIdEq(memberId))
                .fetchOne();
    }

    @Override
    public Page<MemberDto> findAll(MemberSearchDto memberSearchDto, Pageable pageable) {
        QueryResults<MemberDto> result = query
                .select(new QMemberDto(
                        member.memberId,
                        member.name,
                        member.phone,
                        member.email,
                        member.nickname,
                        member.createDt,
                        member.updateDt
                ))
                .from(member)
                .where(
                        nameEq(memberSearchDto.getName())
                        , emailEq(memberSearchDto.getEmail())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(member.createDt.desc())
                .fetchResults();

        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }

    @Override
    public MemberOrderDto findByMemberOrder(final Long memberId, final String orderNum) {
        return query
                .select(new QMemberOrderDto(
                        member.memberId,
                        member.name,
                        member.phone,
                        member.email,
                        member.nickname,
                        member.createDt,
                        member.updateDt,
                        order.orderId,
                        order.orderNum,
                        order.productName,
                        order.orderStatus,
                        order.paymentDt
                ))
                .from(member)
                .innerJoin(member.orders, order)
                .where(
                        memberIdEq(memberId),
                        orderNumEq(orderNum),
                        orderStatusEq()
                ).fetchOne();
    }

    private BooleanExpression memberIdEq(final Long id) {
        if (id <= 0) {
            return null;
        }

        return member.memberId.eq(id);
    }

    private BooleanExpression nameEq(final String name) {
        if (name == null) {
            return null;
        }
        return member.name.eq(name);
    }

    private BooleanExpression emailEq(final String email) {
        if (email == null) {
            return null;
        }
        return member.email.eq(email);
    }

    private BooleanExpression orderStatusEq() {
        return order.orderStatus.eq(OrderStatusEnum.ORDER);
    }

    private BooleanExpression orderNumEq(final String orderNum) {
        if (orderNum == null) {
            return null;
        }
        return order.orderNum.eq(orderNum);
    }
}
