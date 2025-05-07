package com.c.study.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.c.study.document.DocBook;
import com.c.study.entity.Book;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BookMapper extends BaseMapper<Book> {
//    List<DocBook> getList();
//    boolean update(DocBook thing);
}
