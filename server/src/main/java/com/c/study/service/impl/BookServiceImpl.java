package com.c.study.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.c.study.entity.Book;
import com.c.study.entity.ThingTag;
import com.c.study.mapper.BookMapper;
import com.c.study.mapper.ThingTagMapper;
import com.c.study.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl extends ServiceImpl<BookMapper, Book> implements BookService {
    @Autowired
    BookMapper mapper;

    @Autowired
    ThingTagMapper thingTagMapper;

    @Override
    public List<Book> getThingList(String keyword, String sort, String c, String tag) {
        QueryWrapper<Book> queryWrapper = new QueryWrapper<>();

        // 搜索
        queryWrapper.like(StringUtils.isNotBlank(keyword), "title", keyword);

        // 排序
        if (StringUtils.isNotBlank(sort)) {
            if (sort.equals("recent")) {
                queryWrapper.orderBy(true, false, "create_time");
            } else if (sort.equals("hot") || sort.equals("recommend")) {
                queryWrapper.orderBy(true, false, "pv");
            }
        }else {
            queryWrapper.orderBy(true, false, "create_time");
        }

        // 根据分类筛选
        if (StringUtils.isNotBlank(c) && !c.equals("-1")) {
            queryWrapper.eq(true, "classification_id", c);
        }

        List<Book> books = mapper.selectList(queryWrapper);

        // tag筛选
        if (StringUtils.isNotBlank(tag)) {
            List<Book> tBooks = new ArrayList<>();
            QueryWrapper<ThingTag> thingTagQueryWrapper = new QueryWrapper<>();
            thingTagQueryWrapper.eq("tag_id", tag);
            List<ThingTag> thingTagList = thingTagMapper.selectList(thingTagQueryWrapper);
            for (Book book : books) {
                for (ThingTag thingTag : thingTagList) {
                    if (book.getId().equals(thingTag.getThingId())) {
                        tBooks.add(book);
                    }
                }
            }
            books.clear();
            books.addAll(tBooks);
        }

        // 附加tag
        for (Book book : books) {
            QueryWrapper<ThingTag> thingTagQueryWrapper = new QueryWrapper<>();
            thingTagQueryWrapper.lambda().eq(ThingTag::getThingId, book.getId());
            List<ThingTag> thingTags = thingTagMapper.selectList(thingTagQueryWrapper);
            List<Long> tags = thingTags.stream().map(ThingTag::getTagId).collect(Collectors.toList());
            book.setTags(tags);
        }
        return books;
    }

    @Override
    public void createBook(Book book) {
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
