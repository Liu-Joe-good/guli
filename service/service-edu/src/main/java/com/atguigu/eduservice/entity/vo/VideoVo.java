package com.atguigu.eduservice.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="VideoVo对象", description="小节")
public class VideoVo {
    @ApiModelProperty(value = "小节id")
    private String id;

    @ApiModelProperty(value = "小节标题")
    private String title;

}
