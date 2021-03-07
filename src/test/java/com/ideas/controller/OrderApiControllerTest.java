package com.ideas.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ideas.domain.member.Member;
import com.ideas.domain.member.repository.MemberRepository;
import com.ideas.domain.order.Order;
import com.ideas.domain.order.dto.OrderParamDto;
import com.ideas.domain.order.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional(readOnly = true)
public class OrderApiControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    @Transactional
    @DisplayName("정상적으로 주문을 생성하는 테스트")
    public void createOrder() throws Exception {
        Member member = saveMember(1);
        // Given
        OrderParamDto orderInsertDto = OrderParamDto.builder()
                .orderNum("TEST12345678")
                .productCount(3L)
                .productName("Test Product")
                .build();

        // When
        ResultActions perform = mockMvc.perform(post("/api/{id}/order", member.getMemberId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaTypes.HAL_JSON_VALUE)
                .content(this.objectMapper.writeValueAsString(orderInsertDto)))
                .andDo(print());

        // Then
        perform.andExpect(status().isCreated())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("memberId").value(member.getMemberId()))
                .andExpect(jsonPath("orderId").exists())
                .andExpect(jsonPath("orderNum").value(orderInsertDto.getOrderNum()))
                .andExpect(jsonPath("productName").value(orderInsertDto.getProductName()))
                .andExpect(jsonPath("productCount").value(orderInsertDto.getProductCount()))
                .andExpect(jsonPath("_links.self").exists())
                .andDo(print())
        ;
    }

    @Test
    @Transactional
    @DisplayName("주문이 이미 생성되었는데 다시 생성하면 에러가 발생하는 테스트")
    public void createOrder_overlap_bad_Request() throws Exception {
        // Given
        Member member = saveMember(1);
        OrderParamDto orderInsertDto = OrderParamDto.builder()
                .orderNum("TEST12345678")
                .productCount(3L)
                .productName("Test Product")
                .build();
        Order order = Order.createOrder(member, orderInsertDto.getOrderNum(), orderInsertDto.getProductName(), orderInsertDto.getProductCount());
        order = orderRepository.save(order);

        // When & Then
        this.mockMvc.perform(post("/api/{id}/order", member.getMemberId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderInsertDto)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @Transactional
    @DisplayName("입력받을 수 없는 값을 사용한 경우에 에러가 발생하는 테스트")
    public void createOrder_bad_Request() throws Exception {
        // Given
        Member member = saveMember(1);

        // When & Then
        this.mockMvc.perform(post("/api/{id}/order", member.getMemberId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(member)))
                .andExpect(status().isBadRequest())
                .andDo(print())
        ;
    }

    @Test
    @Transactional
    @DisplayName("입력값이 잘못들어가 에러가 발생하는 테스트")
    public void createOrder_bad_Request_Wrong_Input() throws Exception {
        // Given
        Member member = saveMember(1);
        OrderParamDto orderInsertDto = OrderParamDto.builder()
                .orderNum("TEST12345678")
                .productCount(3L)
                .productName("Test fffffffffffffffffffffffffffssssssssssssss")
                .build();

        // When & Then
        this.mockMvc.perform(post("/api/{id}/order", member.getMemberId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(member)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }


    @Test
    @Transactional
    @DisplayName("입력값이 비어있는 경우에 에러가 발생하는 테스트")
    public void createMember_Bad_Request_Empty_Input() throws Exception {
        // Given
        Member member = saveMember(1);
        OrderParamDto orderInsertDto = OrderParamDto.builder().build();

        // When & Then
        this.mockMvc.perform(post("/api/{id}/order", member.getMemberId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderInsertDto)))
                .andExpect(status().isBadRequest())
                .andDo(print());
        ;
    }

    @Test
    @Transactional
    @DisplayName("정상적으로 주문을 수정하는 테스트")
    public void updateOrder() throws Exception {
        // Given
        Member member = saveMember(1);
        OrderParamDto orderInsertDto = OrderParamDto.builder()
                .orderNum("TEST12345678")
                .productCount(3L)
                .productName("Test Product")
                .build();
        Order order = Order.createOrder(member, orderInsertDto.getOrderNum(), orderInsertDto.getProductName(), orderInsertDto.getProductCount());
        orderRepository.save(order);
        OrderParamDto orderUpdateDto = OrderParamDto.builder()
                .orderNum("TEST12345678")
                .productCount(3L)
                .productName("update Product")
                .build();

        // When
        ResultActions perform = mockMvc.perform(put("/api/{member_id}/order/", member.getMemberId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaTypes.HAL_JSON_VALUE)
                .content(this.objectMapper.writeValueAsString(orderUpdateDto)))
                .andDo(print());

        // Then
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("memberId").value(member.getMemberId()))
                .andExpect(jsonPath("orderId").exists())
                .andExpect(jsonPath("orderNum").value(orderUpdateDto.getOrderNum()))
                .andExpect(jsonPath("productName").value(orderUpdateDto.getProductName()))
                .andExpect(jsonPath("productCount").value(orderUpdateDto.getProductCount()))
                .andExpect(jsonPath("_links.self").exists())
                .andDo(print())
        ;
    }

    @Test
    @Transactional
    @DisplayName("입력 받을 수 없는 값을 사용한 경우에 에러가 발생한하는 테스트")
    public void updateOrder_Bad_Request() throws Exception {
        // Given
        Member member = saveMember(1);

        // When & Then
        this.mockMvc.perform(put("/api/member/{id}", String.valueOf(member.getMemberId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(member)))
                .andExpect(status().isBadRequest())
                .andDo(print())
        ;
    }

    private Member saveMember(int index) {
        Member member = Member.builder()
                .name("테스트" + index)
                .nickname("테스트")
                .email("테스트")
                .password("test1234")
                .phone("01068140330")
                .mockMvcBuilder();

        return memberRepository.save(member);
    }
}