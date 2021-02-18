package com.atguigu.eduservice.entity.vo;

import com.atguigu.eduservice.entity.EduVideo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="ChapterVo对象", description="章节")
public class ChapterVo {

    @ApiModelProperty(value = "章节id")
    private String id;

    @ApiModelProperty(value = "章节标题")
    private String title;

    @ApiModelProperty(value = "小节列表")
    private List<VideoVo> list;
}
