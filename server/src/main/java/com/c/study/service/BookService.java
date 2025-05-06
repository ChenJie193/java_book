package com.c.study.service;


import com.c.study.entity.Book;

import java.util.List;

public interface BookService {
    List<Book> getThingList(String keyword, String sort, String c, String tag);
    void createBook(Book book);
    void deleteBook(String id);

    void updateBook(Book book);

    Book getBookById(String id);

    void addWishCount(String thingId);

    void addCollectCount(String thingId);
}
