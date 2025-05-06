package com.c.study.service;


import com.c.study.entity.Book;

import java.util.List;

public interface ThingService {
    List<Book> getThingList(String keyword, String sort, String c, String tag);
    void createThing(Book book);
    void deleteThing(String id);

    void updateThing(Book book);

    Book getThingById(String id);

    void addWishCount(String thingId);

    void addCollectCount(String thingId);
}
