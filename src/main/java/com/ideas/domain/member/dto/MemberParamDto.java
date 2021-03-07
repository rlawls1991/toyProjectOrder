package com.ideas.domain.member.dto;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberParamDto {
    @NotEmpty
    @Size(min = 1, max = 20, message = "이름의 최대길이는 20글자 입니다.")
    private String name;
    @NotEmpty
    @Size(min = 1, max = 30, message = "닉네임의 최대길이는 30글자 입니다.")
    private String nickname;
    @NotEmpty
    @Size(min = 10, message = "비밀번호는 최소 10글자 이상 입니다.")
    private String password;
    @NotEmpty
    @Size(min = 1, max = 13, message = "전화번호의 최대길이는 최대 20글자 입니다.")
    private String phone;
    @Email
    @Size(max = 100, message = "이메일의 최대 길이는 100글자 입니다.")
    private String email;
}
