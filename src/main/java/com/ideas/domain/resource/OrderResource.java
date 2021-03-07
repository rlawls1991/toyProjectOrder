package com.ideas.domain.resource;

import com.ideas.domain.order.dto.OrderDto;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class OrderResource extends EntityModel<OrderDto> {
    public OrderResource(OrderDto orderDto, Link... links) {
        super(orderDto, links);
        add(linkTo(OrderDto.class).slash(orderDto.getOrderId() + "/order").withSelfRel());
    }
}
