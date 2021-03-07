package com.ideas.controller;


import com.ideas.Validator.OrderValidator;
import com.ideas.domain.member.dto.MemberOrderDto;
import com.ideas.domain.order.dto.OrderDto;
import com.ideas.domain.order.dto.OrderParamDto;
import com.ideas.domain.order.service.OrderService;
import com.ideas.domain.resource.OrderResource;
import com.ideas.error.ApiResponseMessage;
import com.ideas.error.ErrorsResource;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;


@RestController
@RequestMapping(value = "/api", consumes = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderService orderService;
    private final OrderValidator orderValidator;

    /**
     * 제품 주문
     *
     * @param orderParamDto
     * @param errors
     * @return
     */
    @PostMapping(value = "/{memberId}/order")
    public ResponseEntity createOrder(@PathVariable Long memberId, @Valid @RequestBody OrderParamDto orderParamDto, Errors errors) {
        if (errors.hasErrors()) {
            return badRequest(errors);
        }
        orderValidator.validate(orderParamDto, errors);
        if (errors.hasErrors()) {
            return badRequest(errors);
        }
        MemberOrderDto memberOrderDto = orderService.findByMemberOrder(memberId, orderParamDto.getOrderNum());
        if (memberOrderDto != null) {
            ApiResponseMessage message = new ApiResponseMessage("Fail", "overlap order", "", "");
            return new ResponseEntity<ApiResponseMessage>(message, HttpStatus.BAD_REQUEST);
        }

        OrderDto orderDto = orderService.createOrder(memberId, orderParamDto);
        OrderResource orderResource = new OrderResource(orderDto);
        WebMvcLinkBuilder selfLinkBuilder = linkTo(OrderApiController.class).slash(orderDto.getMemberId() + "/order");
        URI createdUri = selfLinkBuilder.toUri();
        orderResource.add(selfLinkBuilder.withRel("get-order"));
        orderResource.add(selfLinkBuilder.withRel("update-order"));
        orderResource.add(selfLinkBuilder.withRel("delete-order"));

        return ResponseEntity.created(createdUri).body(orderResource);
    }

    /**
     * 제품 수량 변경
     *
     * @param orderParamDto
     * @param errors
     * @return
     */
    @PutMapping(value = "/{memberId}/order")
    public ResponseEntity updateOrder(@PathVariable Long memberId, @Valid @RequestBody OrderParamDto orderParamDto, Errors errors) {
        if (errors.hasErrors()) {
            return badRequest(errors);
        }
        orderValidator.validate(orderParamDto, errors);
        if (errors.hasErrors()) {
            return badRequest(errors);
        }
        MemberOrderDto memberOrderDto = orderService.findByMemberOrder(memberId, orderParamDto.getOrderNum());
        if (memberOrderDto == null) {
            ApiResponseMessage message = new ApiResponseMessage("Fail", "none order", "", "");
            return new ResponseEntity<ApiResponseMessage>(message, HttpStatus.BAD_REQUEST);
        }

        OrderDto orderDto = orderService.updateOrder(memberId, orderParamDto);
        OrderResource orderResource = new OrderResource(orderDto);
        WebMvcLinkBuilder selfLinkBuilder = linkTo(OrderApiController.class).slash(orderDto.getMemberId() + "/order");
        orderResource.add(selfLinkBuilder.withRel("create-order"));
        orderResource.add(selfLinkBuilder.withRel("get-order"));
        orderResource.add(selfLinkBuilder.withRel("delete-order"));

        return ResponseEntity.ok(orderResource);
    }




    private ResponseEntity<?> badRequest(Errors errors) {
        return ResponseEntity.badRequest().body(new ErrorsResource(errors));
    }
}
