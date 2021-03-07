package com.ideas.Validator;

import com.ideas.domain.member.dto.MemberParamDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.util.regex.Pattern;

@Component
public class MemberValidator {

    public void validate(final MemberParamDto memberParamDto, final Errors errors) {
        checkPhone(memberParamDto, errors);
    }

    private void checkPhone(final MemberParamDto memberParamDto, final Errors errors) {
        if (!isNumber(memberParamDto.getPhone())) {
            errors.rejectValue("phone", "wrongValue", "전화번호는 숫자만 입력받을 수 있습니다.");
        }
    }

    private boolean isNumber(String param) {
        return Pattern.matches("^[0-9]*$", param);
    }
}
