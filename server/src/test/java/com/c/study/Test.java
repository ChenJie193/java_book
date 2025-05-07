package com.c.study;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.c.study.document.DocBook;
import com.c.study.entity.Book;
import com.c.study.mapper.BookMapper;
import com.c.study.repository.BookRepository;
import com.c.study.service.BookService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class Test {

    @Autowired
    private BookMapper mapper;
    @Autowired
    private BookRepository bookRepository;

    @org.junit.jupiter.api.Test
    void test(){
        List<Book> title = mapper.selectList(new QueryWrapper<Book>().isNotNull("title"));
        title.stream().forEach(i -> {
            DocBook docBook = new DocBook();
            BeanUtils.copyProperties(i,docBook);
            DocBook save = bookRepository.save(docBook);
            System.out.println(save);
        });
        System.out.println("成功");
    }
}
