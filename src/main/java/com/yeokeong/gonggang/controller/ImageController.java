package com.yeokeong.gonggang.controller;

import com.yeokeong.gonggang.security.SessionUser;
import com.yeokeong.gonggang.services.PlaceService;
import com.yeokeong.gonggang.services.PostService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ImageController {
    private final PostService postService;
    private final PlaceService placeService;

    @ApiOperation("글 이미지 추가")
    @PostMapping(value = "/v1/posts/{seq}/pictures", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> createPostPictures(
            @ApiParam(hidden = true) @SessionUser Long userSeq,
            @PathVariable Long seq,
            @RequestPart List<MultipartFile> imageFiles
    ) {
        postService.createPostPictures(userSeq, seq, imageFiles);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("장소 이미지 추가")
    @PostMapping(value = "/v1/place/{seq}/pictures", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> createPlacePictures(
            @ApiParam(hidden = true) @SessionUser Long userSeq,
            @PathVariable Long seq,
            @RequestPart List<MultipartFile> imageFiles
    ) {
        placeService.createPlacePictures(seq,imageFiles);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
