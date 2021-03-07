package com.ideas.Validator;

import com.ideas.domain.order.dto.OrderParamDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.util.regex.Pattern;

@Component
public class OrderValidator {

    private static final String pattern = "^[0-9A-Z]*$";

    public void validate(final OrderParamDto orderParamDto, final Errors errors) {
        checkOrderNum(orderParamDto.getOrderNum(), errors);
    }

    private void checkOrderNum(String orderNum, final Errors errors) {
        if (!Pattern.matches(pattern, orderNum)) {
            errors.rejectValue("orderNum", "wrongValue", "주문번호는 영문대문자, 숫자의 조합으로만 가능합니다.");
        }
    }
}