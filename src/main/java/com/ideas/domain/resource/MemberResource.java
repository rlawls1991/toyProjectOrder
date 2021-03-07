package com.ideas.domain.resource;

import com.ideas.controller.MemberApiController;
import com.ideas.domain.member.dto.MemberDto;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class MemberResource extends EntityModel<MemberDto> {
    public MemberResource(MemberDto memberDto, Link... links) {
        super(memberDto, links);
        add(linkTo(MemberApiController.class).slash(memberDto.getMemberId()).withSelfRel());
    }
}