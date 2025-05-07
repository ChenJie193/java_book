package com.c.study.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.c.study.document.DocBook;
import com.c.study.entity.Book;
import com.c.study.entity.ThingTag;
import com.c.study.mapper.BookMapper;
import com.c.study.mapper.ThingTagMapper;
import com.c.study.repository.BookRepository;
import com.c.study.service.BookService;
import org.apache.lucene.util.QueryBuilder;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl extends ServiceImpl<BookMapper, Book> implements BookService {
    @Autowired
    private BookMapper mapper;
    @Autowired
    private ThingTagMapper thingTagMapper;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private ElasticsearchRestTemplate restTemplate;

    @Override
    public List<Book> getBookList(String keyword, String sort, String c, String tag) {
        BoolQueryBuilder builder = QueryBuilders.boolQuery();
        if(Objects.nonNull(keyword)){
            builder.should(QueryBuilders.multiMatchQuery(keyword,"title","description","author","author","press"));
        }
        if(Objects.nonNull(c) && !c.equals("-1")){
            builder.must(QueryBuilders.termQuery("classificationId",c));
        }
        if(Objects.equals(c,"-1")){
            builder.must(QueryBuilders.matchAllQuery());
        }
        FieldSortBuilder order = SortBuilders.fieldSort("createTime").order(SortOrder.DESC);
        NativeSearchQueryBuilder nativeBuilder = new NativeSearchQueryBuilder();
        NativeSearchQuery build = nativeBuilder.withQuery(builder).withSort(order).build();
        SearchHits<DocBook> search = restTemplate.search(build, DocBook.class);
        List<DocBook> collect = search.stream().map(n -> n.getContent()).collect(Collectors.toList());
        List<Book> books = collect.stream().map(i -> {
            Book book = new Book();
            BeanUtils.copyProperties(i, book);
            return book;
        }).collect(Collectors.toList());
        return books;
    }

    @Override
    public void createBook(Book book) {
        DocBook docBook = new DocBook();
        System.out.println(book);
        book.setCreateTime(String.valueOf(System.currentTimeMillis()));

        if (book.getPv() == null) {
            book.setPv("0");
        }
        if (book.getScore() == null) {
            book.setScore("0");
        }
        if (book.getWishCount() == null) {
            book.setWishCount("0");
        }
        mapper.insert(book);
        BeanUtils.copyProperties(book,docBook);
        DocBook save = bookRepository.save(docBook);
        // 更新tag
        setThingTags(book);
    }

    @Override
    public void deleteBook(String id) {
        mapper.deleteById(id);
    }

    @Override
    public void updateBook(Book book) {

        // 更新tag
        setThingTags(book);

        mapper.updateById(book);
    }

    @Override
    public Book getBookById(String id) {
        return mapper.selectById(id);
    }

    // 心愿数加1
    @Override
    public void addWishCount(String thingId) {
        Book book = mapper.selectById(thingId);
        book.setWishCount(String.valueOf(Integer.parseInt(book.getWishCount()) + 1));
        mapper.updateById(book);
    }

    // 收藏数加1
    @Override
    public void addCollectCount(String thingId) {
        Book book = mapper.selectById(thingId);
        book.setCollectCount(String.valueOf(Integer.parseInt(book.getCollectCount()) + 1));
        mapper.updateById(book);
    }

    public void setThingTags(Book book) {
        // 删除tag
        Map<String, Object> map = new HashMap<>();
        map.put("thing_id", book.getId());
        thingTagMapper.deleteByMap(map);
        // 新增tag
        if (book.getTags() != null) {
            for (Long tag : book.getTags()) {
                ThingTag thingTag = new ThingTag();
                thingTag.setThingId(book.getId());
                thingTag.setTagId(tag);
                thingTagMapper.insert(thingTag);
            }
        }
    }

}
