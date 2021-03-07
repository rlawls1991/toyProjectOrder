package com.ideas.domain.order;


import com.ideas.domain.member.Member;
import com.ideas.domain.order.dto.OrderParamDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "orders")
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @Column(name = "order_num", nullable = true, length = 12)
    private String orderNum; // 주문번호

    @Column(name = "product_name", nullable = true, length = 100)
    private String productName; // 제품명

    @Column(name = "product_count")
    private Long productCount; // 제품 갯수

    @CreationTimestamp
    @Column(name = "payment_dt")
    private LocalDateTime paymentDt; // 구매일시

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatusEnum orderStatus;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "member_id")
    private Member member;

    // 연관관계 메서드
    private void setMember(final Member member) {
        this.member = member;
    }

    public static Order createOrder(final Member member, final String orderNum, final String productName, final Long productCount) {
        Order order = new Order();
        order.setMember(member);
        order.orderNum = orderNum;
        order.productName = productName;
        order.productCount = productCount;
        order.orderStatus = OrderStatusEnum.ORDER;

        return order;
    }

    // 제품 수정
    public void updateOrder(final OrderParamDto dto) {
        this.productCount = dto.getProductCount();
        this.productName = dto.getProductName();
    }
}
