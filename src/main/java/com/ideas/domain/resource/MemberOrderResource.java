package com.ideas.domain.resource;

import com.ideas.controller.MemberApiController;
import com.ideas.domain.member.dto.MemberOrderDto;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class MemberOrderResource extends EntityModel<MemberOrderDto> {
    public MemberOrderResource(MemberOrderDto memberOrderDto, Link... links) {
        super(memberOrderDto, links);
        add(linkTo(MemberApiController.class).slash(memberOrderDto.getMemberId()).withSelfRel());
    }
}
