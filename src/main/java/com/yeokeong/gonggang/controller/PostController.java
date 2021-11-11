package com.yeokeong.gonggang.controller;

import com.yeokeong.gonggang.model.req.ReqPostCreate;
import com.yeokeong.gonggang.model.req.ReqPostList;
import com.yeokeong.gonggang.model.res.ResPost;
import com.yeokeong.gonggang.model.res.ResPostList;
import com.yeokeong.gonggang.security.SessionUser;
import com.yeokeong.gonggang.services.PostService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @ApiOperation("인기글리스트보기")
    @GetMapping(value = "/v1/best/posts")
    public List<ResPostList> getBestPostList(
            @ApiParam(hidden = true) @SessionUser Long userSeq
    ){
        return postService.getBestPostList(userSeq)
                .stream()
                .map(ResPostList::of)
                .collect(Collectors.toList());
    }

    @ApiOperation("추천글리스트보기")
    @GetMapping(value = "/v1/recommand/posts")
    public List<ResPostList> getRecommandPostList(
            @ApiParam(hidden = true) @SessionUser Long userSeq
    ){
        return postService.getRecommandPostList(userSeq)
                .stream()
                .map(ResPostList::of)
                .collect(Collectors.toList());
    }

    @ApiOperation("북마크글리스트보기")
    @GetMapping(value = "/v1/bookmark/posts")
    public List<ResPostList> getBookmarkPostList(
            @ApiParam(hidden = true) @SessionUser Long userSeq
    ){
        return postService.getBookmarkPostList(userSeq)
                .stream()
                .map(ResPostList::of)
                .collect(Collectors.toList());
    }

    @ApiOperation("글리스트보기")
    @GetMapping(value = "/v1/posts")
    public List<ResPostList> getPostList(
            @ApiParam(hidden = true) @SessionUser Long userSeq,
            @Valid ReqPostList req
    ){
        return postService.getPostList(userSeq, req)
                .stream()
                .map(ResPostList::of)
                .collect(Collectors.toList());
    }

    @ApiOperation("글보기")
    @GetMapping(value = "/v1/posts/{seq}")
    public ResPost getPost(
            @ApiParam(hidden = true) @SessionUser Long userSeq,
            @PathVariable Long seq
    ){
        return ResPost.of(postService.getPost(userSeq, seq));
    }

    @ApiOperation("글작성")
    @PostMapping(value = "/v1/posts", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createPost(
            @ApiParam(hidden = true) @SessionUser Long userSeq,
            @RequestBody @Valid ReqPostCreate reqPostCreate
    ) {
        try {
            postService.createPost(userSeq, reqPostCreate);
        }catch (IOException e){

        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

//    @ApiOperation("글수정")
//    @PutMapping(value = "/v1/posts/{seq}", consumes = MediaType.APPLICATION_JSON_VALUE)

    @ApiOperation("글삭제")
    @DeleteMapping(value = "/v1/posts/{seq}")
    public ResponseEntity<Void> createPost(
            @ApiParam(hidden = true) @SessionUser Long userSeq,
            @PathVariable Long seq
    ) {
        postService.deletePost(userSeq, seq);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("글 좋아요")
    @PostMapping(value = "/v1/posts/{seq}/like")
    public ResponseEntity<Void> createPostLike(
            @ApiParam(hidden = true) @SessionUser Long userSeq,
            @PathVariable Long seq
    ) {
        postService.likePost(userSeq, seq);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("글 좋아요 취소")
    @DeleteMapping(value = "/v1/posts/{seq}/like")
    public ResponseEntity<Void> deletePostLike(
            @ApiParam(hidden = true) @SessionUser Long userSeq,
            @PathVariable Long seq
    ) {
        postService.unlikePost(userSeq, seq);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("글 북마크")
    @PostMapping(value = "/v1/posts/{seq}/bookmark")
    public ResponseEntity<Void> createPostBookmark(
            @ApiParam(hidden = true) @SessionUser Long userSeq,
            @PathVariable Long seq
    ) {
        postService.createPostBookmark(userSeq, seq);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("글 북마크 취소")
    @DeleteMapping(value = "/v1/posts/{seq}/bookmark")
    public ResponseEntity<Void> deletePostBookmark(
            @ApiParam(hidden = true) @SessionUser Long userSeq,
            @PathVariable Long seq
    ) {
        postService.deletePostBookmark(userSeq, seq);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
