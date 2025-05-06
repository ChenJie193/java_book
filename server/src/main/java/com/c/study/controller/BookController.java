package com.c.study.controller;

import com.c.study.common.APIResponse;
import com.c.study.common.ResponeCode;
import com.c.study.document.DocBook;
import com.c.study.entity.Book;
import com.c.study.permission.Access;
import com.c.study.permission.AccessLevel;
import com.c.study.repository.BookRepository;
import com.c.study.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/book")
public class BookController {

    private final static Logger logger = LoggerFactory.getLogger(BookController.class);

    @Autowired
    private BookService service;
    @Autowired
    private BookRepository bookRepository;

    @Value("${File.uploadPath}")
    private String uploadPath;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public APIResponse list(String keyword, String sort, String c, String tag){
        List<Book> list =  service.getThingList(keyword, sort, c, tag);

        return new APIResponse(ResponeCode.SUCCESS, "查询成功", list);
    }

    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public APIResponse detail(String id){
        Book book =  service.getBookById(id);

        return new APIResponse(ResponeCode.SUCCESS, "查询成功", book);
    }

    @Access(level = AccessLevel.ADMIN)
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @Transactional
    public APIResponse create(Book book) throws IOException {
        DocBook docBook = new DocBook();

        service.createBook(book);
        BeanUtils.copyProperties(book,docBook);
        DocBook save = bookRepository.save(docBook);
        return new APIResponse(ResponeCode.SUCCESS, "创建成功");
    }

    @Access(level = AccessLevel.ADMIN)
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public APIResponse delete(String ids){
        System.out.println("ids===" + ids);
        // 批量删除
        String[] arr = ids.split(",");
        for (String id : arr) {
            service.deleteBook(id);
        }
        return new APIResponse(ResponeCode.SUCCESS, "删除成功");
    }

    @Access(level = AccessLevel.ADMIN)
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @Transactional
    public APIResponse update(Book book) throws IOException {
        DocBook docBook = new DocBook();
        System.out.println(book);
//        String url = saveBook(book);
//        if(!StringUtils.isEmpty(url)) {
//            book.setCover(url);
//        }

        service.updateBook(book);
        BeanUtils.copyProperties(book, docBook);
        bookRepository.save(docBook);
        return new APIResponse(ResponeCode.SUCCESS, "更新成功");
    }

}
