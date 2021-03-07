package com.ideas.domain.member.repository;


import com.ideas.domain.member.dto.MemberDto;
import com.ideas.domain.member.dto.MemberOrderDto;
import com.ideas.domain.member.dto.MemberSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface MemberRepositoryCustom {

    /**
     * 회원 한명의 정보를 조회
     *
     * @param memberId
     * @return
     */
    MemberDto findByMember(final Long memberId);


    /**
     * 회원 페이징 형태로 조회
     *
     * @param memberSearchDto
     * @param pageable
     * @return
     */
    Page<MemberDto> findAll(final MemberSearchDto memberSearchDto, final Pageable pageable);

    /**
     * 회원의 주문 정보를 조회
     * 참고, 이 로직은 주문 한것만 찾는 로직(필요하다면 취소한 것도 추가)
     *
     * @param memberId
     * @param orderNum
     * @return
     */
    MemberOrderDto findByMemberOrder(final Long memberId, final String orderNum);
}
