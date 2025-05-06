package com.c.study.repository;

import com.c.study.document.DocBook;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends ElasticsearchRepository<DocBook, Long> {

}
