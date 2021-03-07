package com.ideas.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.ideas.domain.member.Member;
import com.ideas.domain.member.dto.MemberParamDto;
import com.ideas.domain.member.repository.MemberRepository;
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

import java.util.stream.IntStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional(readOnly = true)
public class MemberApiControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @Transactional
    @DisplayName("정상적으로 회원을 생성하는 테스트")
    public void createMember() throws Exception {
        // Given
        MemberParamDto member = MemberParamDto.builder()
                .name("테스트")
                .nickname("테스트")
                .email("test@email.com")
                .password("test12346789")
                .phone("01012345678")
                .build();

        // When
        ResultActions perform = mockMvc.perform(post("/api/member")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaTypes.HAL_JSON_VALUE)
                .content(this.objectMapper.writeValueAsString(member)))
                .andDo(print());

        // Then
        perform.andExpect(status().isCreated())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("name").value(member.getName()))
                .andExpect(jsonPath("nickname").value(member.getNickname()))
                .andExpect(jsonPath("email").value(member.getEmail()))
                .andExpect(jsonPath("phone").value(member.getPhone()))
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.update-member").exists())
                .andDo(print())
        ;
    }

    @Test
    @Transactional
    @DisplayName("입력 받을 수 없는 값을 사용한 경우에 에러가 발생하는 테스트")
    public void createMember_bad_Request() throws Exception {
        // Given
        Member member = saveMember(1);

        // When
        ResultActions perform = mockMvc.perform(post("/api/member")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaTypes.HAL_JSON_VALUE)
                .content(this.objectMapper.writeValueAsString(member)))
                .andDo(print());
        // Then
        perform.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("입력값이 비어있는 경우에 에러가 발생하는 테스트")
    public void createMember_Bad_Request_Empty_Input() throws Exception {
        // Given
        Member member = saveMember(1);

        // When & Then
        this.mockMvc.perform(post("/api/member")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(member)))
                .andExpect(status().isBadRequest())
                .andDo(print());
        ;
    }

    @Test
    @DisplayName("입력값이 잘못들어가 에러가 발생하는 테스트")
    public void createMember_Bad_Request_Wrong_Input() throws Exception {
        // Given
        Member member = saveMember(1);

        // When & Then
        this.mockMvc.perform(post("/api/member")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(member)))
                .andExpect(status().isBadRequest())
                .andDo(print());
        ;
    }

    @Test
    @Transactional
    @DisplayName("30명의 회원들을 10개식 조회하기")
    public void queryMembers() throws Exception {
        IntStream.range(1, 41).forEach(this::saveMember);

        // When
        ResultActions perform = mockMvc.perform(get("/api/member")
                .param("page", "1")
                .param("size", "10")
                .param("email", "테스트")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON))
                .andDo(print());

        // Then
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("page").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andDo(print())
        ;
    }

    @Test
    @Transactional
    @DisplayName("기존의 회원를 하나 조회하기")
    public void getMember() throws Exception {
        // Given
        Member member = saveMember(100);

        // When & Then
        this.mockMvc.perform(get("/api/member/{id}", member.getMemberId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").exists())
                .andExpect(jsonPath("memberId").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andDo(print())
        ;
    }

    @Test
    @DisplayName("없는 회원을 조회했을 때 404 응답")
    public void getMember404() throws Exception {
        // When & Then
        this.mockMvc.perform(get("/api/member/{id}", 9999)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print())
        ;
    }

    @Test
    @Transactional
    @DisplayName("데이터 수정")
    public void updateMember() throws Exception {
        // Given
        Member member = saveMember(100);
        MemberParamDto memberUpdateDto = MemberParamDto.builder()
                .name("수정")
                .nickname("수정")
                .email("update@email.com")
                .password("update123445")
                .phone("01043218765")
                .build();

        // When
        ResultActions perform = this.mockMvc.perform(put("/api/member/{id}", String.valueOf(member.getMemberId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(memberUpdateDto)))
                .andDo(print());

        // Then
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("name").value(memberUpdateDto.getName()))
                .andExpect(jsonPath("email").value(memberUpdateDto.getEmail()))
                .andExpect(jsonPath("nickname").value(memberUpdateDto.getNickname()))
                .andExpect(jsonPath("phone").value(memberUpdateDto.getPhone()))
                .andExpect(jsonPath("_links.self").exists())
                .andDo(print())
        ;
    }

    @Test
    @Transactional
    @DisplayName("입력 받을 수 없는 값을 사용한 경우에 에러가 발생한하는 테스트")
    public void updateMember_Bad_Request() throws Exception {
        // When
        Member member = saveMember(100);

        // When & Then
        this.mockMvc.perform(put("/api/member/{id}", String.valueOf(member.getMemberId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(member)))
                .andExpect(status().isBadRequest())
                .andDo(print())
        ;
    }

    @Test
    @Transactional
    @DisplayName("없는 회원을 수정했을 때 404응답")
    public void updateMember404() throws Exception {
        // Given
        MemberParamDto memberUpdateDto = MemberParamDto.builder()
                .name("수정")
                .nickname("수정")
                .email("update@email.com")
                .password("update123445")
                .phone("01043218765")
                .build();


        // When & Then
        this.mockMvc.perform(put("/api/member/{id}", 1004)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(memberUpdateDto)))
                .andExpect(status().isNotFound())
                .andDo(print())
        ;
    }

    @Test
    @Transactional
    @DisplayName("기존의 회원를 하나 삭제하기")
    public void deleteMember() throws Exception {
        // Given
        Member member = saveMember(100);

        this.mockMvc.perform(delete("/api/member/{id}", member.getMemberId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("_links.self").exists())
                .andDo(print())
        ;
    }

    @Test
    @DisplayName("없는 회원을 삭제했을 때 404 응답")
    public void deleteStudent404() throws Exception {
        // When & Then
        this.mockMvc.perform(delete("/api/member/{id}", 9999)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
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