package com.c.study.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.sql.Date;

@Data
@TableName("tb_classification")
public class Classification implements Serializable {
    @TableId(value = "id",type = IdType.AUTO)
    public Long id;
    @TableField
    public String title;
    @TableField
    public String createTime;

}
