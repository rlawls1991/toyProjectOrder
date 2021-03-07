package com.ideas.domain.member;


import com.ideas.domain.member.dto.MemberParamDto;
import com.ideas.domain.order.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "member")
@AllArgsConstructor
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long memberId;
    @Column(nullable = true, length = 20)
    private String name;
    @Column(nullable = true, length = 30)
    private String nickname;
    private String password;
    @Column(nullable = true, length = 20)
    private String phone;
    @Column(nullable = true, length = 100)
    private String email;

    @CreationTimestamp
    @Column(name = "create_dt")
    private LocalDateTime createDt; // 생성일시
    @UpdateTimestamp
    @Column(name = "update_dt")
    private LocalDateTime updateDt; // 수정일시

    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();

    @Builder(buildMethodName = "mockMvcBuilder")
    private Member(Long memberId, String name, String nickname, String password, String phone, String email) {
        this.memberId = memberId;
        this.name = name;
        this.nickname = nickname;
        this.password = password;
        this.phone = phone;
        this.email = email;
    }

    public Member(String name, String nickname, String password, String phone, String email) {
        this.name = name;
        this.nickname = nickname;
        this.password = password;
        this.phone = phone;
        this.email = email;
    }

    public void updateMember(final MemberParamDto paramDto){
        this.name = paramDto.getName();
        this.nickname = paramDto.getNickname();
        this.password = paramDto.getPassword();
        this.phone = paramDto.getPhone();
        this.email = paramDto.getEmail();
    }

    public static Member createMember(final MemberParamDto paramDto){
        return new Member(paramDto.getName(), paramDto.getNickname(), paramDto.getPassword(), paramDto.getPhone(), paramDto.getEmail());
    }
}
