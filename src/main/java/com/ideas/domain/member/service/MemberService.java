package com.ideas.domain.member.service;

import com.ideas.domain.member.dto.MemberDto;
import com.ideas.domain.member.dto.MemberParamDto;
import com.ideas.domain.member.dto.MemberSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberService {

    /**
     * 회원 가입
     *
     * @param memberParamDto
     * @return
     */
    MemberDto createMember(final MemberParamDto memberParamDto);

    /**
     * 회원 찾기
     *
     * @return
     */
    MemberDto findByMember(final Long id);

    /**
     * 회원 정보 수정
     *
     * @param id
     * @param memberParamDto
     * @return
     */
    MemberDto updateMember(final Long id, MemberParamDto memberParamDto);

    /**
     * 회원 조회 (여러명)
     *
     * @return
     */
    Page<MemberDto> findAll(final MemberSearchDto searchDto, final Pageable pageable);

    /**
     * 회원 삭제
     *
     * @param id
     */
    void deleteMember(final Long id);
}
