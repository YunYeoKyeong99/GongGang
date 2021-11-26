package com.yeokeong.gonggang.model.req;

import com.yeokeong.gonggang.common.CategoryType;
import com.yeokeong.gonggang.common.CostType;
import com.yeokeong.gonggang.common.DurationTimeType;
import com.yeokeong.gonggang.common.TimingType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
public class ReqPostCreate {
    @NotBlank
    private String title; // TODO REGEX
    @NotNull
    private TimingType timingType;
    @NotNull
    private DurationTimeType durationTimeType;
    @NotNull
    private CostType costType;
    @NotNull
    private List<CategoryType> categoryTypeList = new ArrayList<>();
    @NotBlank
    private String content;
    @NotNull
    private String linkUrl; // TODO REGEX

//    private List<ProductBadge> badges = new LinkedList<>();

//    private List<MultipartFile> pictures;
}
