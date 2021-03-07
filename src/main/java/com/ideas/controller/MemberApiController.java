package com.ideas.controller;


import com.ideas.Validator.MemberValidator;
import com.ideas.domain.member.dto.MemberDto;
import com.ideas.domain.member.dto.MemberParamDto;
import com.ideas.domain.member.dto.MemberSearchDto;
import com.ideas.domain.member.service.MemberService;
import com.ideas.domain.resource.MemberResource;
import com.ideas.error.ErrorsResource;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping(value = "/api/member", consumes = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberValidator memberValidator;
    private final MemberService memberService;

    /**
     * 회원 가입
     *
     * @param memberParamDto
     * @param errors
     * @return
     */
    @PostMapping
    public ResponseEntity createMember(@Valid @RequestBody MemberParamDto memberParamDto, Errors errors) {
        if (errors.hasErrors()) {
            return badRequest(errors);
        }
        memberValidator.validate(memberParamDto, errors);
        if (errors.hasErrors()) {
            return badRequest(errors);
        }

        MemberDto saveMember = memberService.createMember(memberParamDto);
        var selfLinkBuilder = linkTo(MemberApiController.class).slash(saveMember.getMemberId());
        URI createdUri = selfLinkBuilder.toUri();
        MemberResource memberResource = new MemberResource(saveMember);
        memberResource.add(selfLinkBuilder.withRel("get-member"));
        memberResource.add(linkTo(MemberApiController.class).withRel("query-member"));
        memberResource.add(selfLinkBuilder.withRel("update-member"));
        memberResource.add(selfLinkBuilder.withRel("delete-member"));

        return ResponseEntity.created(createdUri).body(memberResource);
    }

    /**
     * 회원 조회
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity getMember(@PathVariable Long id) {
        MemberDto member = memberService.findByMember(id);

        if (member == null) {
            return ResponseEntity.notFound().build();
        }

        MemberResource memberResource = new MemberResource(member);
        var selfLinkBuilder = linkTo(MemberApiController.class).slash(member.getMemberId());
        memberResource.add(selfLinkBuilder.withRel("create-member"));
        memberResource.add(linkTo(MemberApiController.class).withRel("query-member"));
        memberResource.add(selfLinkBuilder.withRel("update-member"));
        memberResource.add(selfLinkBuilder.withRel("delete-member"));

        return ResponseEntity.ok(memberResource);
    }

    /**
     * 회원정보 조회(페이징)
     *
     * @param searchDto
     * @param pageable
     * @param assembler
     * @return
     */
    @GetMapping
    public ResponseEntity queryMembers(MemberSearchDto searchDto, Pageable pageable, PagedResourcesAssembler<MemberDto> assembler) {

        Page<MemberDto> memberPages = memberService.findAll(searchDto, pageable);

        var pageResource = assembler.toModel(memberPages, e -> new MemberResource(e));

        return ResponseEntity.ok(pageResource);
    }

    /**
     * 회원 정보 수정
     *
     * @param id
     * @param memberParamDto
     * @param errors
     * @return
     */
    @PutMapping("/{id}")
    public ResponseEntity updateMember(@PathVariable Long id,
                                       @RequestBody @Valid MemberParamDto memberParamDto, Errors errors) {
        if (errors.hasErrors()) {
            return badRequest(errors);
        }
        memberValidator.validate(memberParamDto, errors);
        if (errors.hasErrors()) {
            return badRequest(errors);
        }

        MemberDto member = memberService.findByMember(id);
        if (member == null) {
            return ResponseEntity.notFound().build();
        }

        MemberDto updateMember = memberService.updateMember(id, memberParamDto);
        MemberResource memberResource = new MemberResource(updateMember);
        var selfLinkBuilder = linkTo(MemberApiController.class).slash(member.getMemberId());
        memberResource.add(selfLinkBuilder.withRel("create-member"));
        memberResource.add(selfLinkBuilder.withRel("get-member"));
        memberResource.add(linkTo(MemberApiController.class).withRel("query-member"));
        memberResource.add(selfLinkBuilder.withRel("delete-member"));

        return ResponseEntity.ok(memberResource);
    }

    /**
     * 회원 정보 삭제
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity deleteMember(@PathVariable Long id) {
        MemberDto member = memberService.findByMember(id);
        if (member == null) {
            return ResponseEntity.notFound().build();
        }

        memberService.deleteMember(id);

        MemberResource memberResource = new MemberResource(member);
        var selfLinkBuilder = linkTo(MemberApiController.class).slash(member.getMemberId());
        memberResource.add(selfLinkBuilder.withRel("create-member"));
        memberResource.add(selfLinkBuilder.withRel("get-member"));
        memberResource.add(linkTo(MemberApiController.class).withRel("query-member"));
        memberResource.add(selfLinkBuilder.withRel("update-member"));

        return ResponseEntity.ok(memberResource);
    }


    private ResponseEntity<?> badRequest(Errors errors) {
        return ResponseEntity.badRequest().body(new ErrorsResource(errors));
    }
}