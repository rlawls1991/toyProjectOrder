package com.ideas.domain.order.dto;


import com.ideas.domain.order.Order;
import com.ideas.domain.order.OrderStatusEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderDto {
    private Long memberId;
    private Long orderId;
    private String orderNum;
    private String productName;
    private Long productCount;
    private OrderStatusEnum orderStatus;

    public OrderDto(Long memberId, Long orderId, String orderNum, String productName, Long productCount) {
        this.memberId = memberId;
        this.orderId = orderId;
        this.orderNum = orderNum;
        this.productName = productName;
        this.productCount = productCount;
    }

    public static OrderDto createOrderDto(final Long memberId, final Order order) {
        OrderDto orderDto = new OrderDto(memberId, order.getOrderId(), order.getOrderNum(), order.getProductName(), order.getProductCount());
        orderDto.orderStatus();

        return orderDto;
    }

    public void orderStatus() {
        this.orderStatus = OrderStatusEnum.ORDER;
    }
}
