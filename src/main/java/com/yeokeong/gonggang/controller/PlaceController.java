package com.yeokeong.gonggang.controller;

import com.yeokeong.gonggang.model.req.ReqPlaceList;
import com.yeokeong.gonggang.model.res.ResPlace;
import com.yeokeong.gonggang.model.res.ResPlaceList;
import com.yeokeong.gonggang.model.res.ResPost;
import com.yeokeong.gonggang.model.res.ResPostList;
import com.yeokeong.gonggang.security.SessionUser;
import com.yeokeong.gonggang.services.PlaceService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PlaceController {
    private final PlaceService placeService;

    @ApiOperation("장소리스트보기")
    @GetMapping(value = "/v1/places")
    public List<ResPlaceList> getPlaceList(
            @ApiParam(hidden = true) @SessionUser Long userSeq,
            @Valid ReqPlaceList req
    ){
        return placeService.getPlaceList(userSeq, req)
                .stream()
                .map(ResPlaceList::of)
                .collect(Collectors.toList());
    }

    @ApiOperation("장소보기")
    @GetMapping(value = "/v1/places/{seq}")
    public ResPlace getPlace(
            @ApiParam(hidden = true) @SessionUser Long userSeq,
            @PathVariable Long seq
    ){
        return ResPlace.of(placeService.getPlace(userSeq,seq));
    }


}
