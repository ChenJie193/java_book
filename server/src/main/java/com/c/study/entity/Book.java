package com.c.study.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.List;

@Data
@TableName("tb_book")
public class Book implements Serializable {
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    @TableField
    private String title;
    @TableField
    private String cover;
    @TableField
    private String description;
    @TableField
    private String price;
    @TableField
    private String status;
    @TableField
    private String createTime;
    @TableField
    private String repertory;
    @TableField
    private String translator;
    @TableField
    private String isbn;
    @TableField
    private String layout;
    @TableField
    private String author;
    @TableField
    private String press;
    @TableField
    private String pubDate;
    @TableField
    private String score;
    @TableField
    private String pageCount;
    @TableField
    private String pv;
    @TableField
    private String recommendCount;
    @TableField
    private String wishCount;
    @TableField
    private String collectCount;
    @TableField
    private Long classificationId;

    @TableField(exist = false)
    private List<Long> tags; // 标签

    @TableField(exist = false)
    private String imageFile;

}
