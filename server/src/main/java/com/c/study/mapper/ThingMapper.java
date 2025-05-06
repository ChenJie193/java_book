package com.c.study.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.c.study.entity.Book;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ThingMapper extends BaseMapper<Book> {
//    List<Book> getList();
//    boolean update(Book thing);
}
