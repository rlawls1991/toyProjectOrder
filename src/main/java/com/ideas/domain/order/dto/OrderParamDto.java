package com.ideas.domain.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderParamDto {
    @NotEmpty
    @Size(min = 12, max = 12, message = "제품번호는 12글자 입니다.")
    private String orderNum; // 주문번호
    @NotEmpty
    private String productName; // 제품명
    @Min(1)
    private Long productCount; // 제품 주문갯수
}
