package com.c.study.service;


import com.c.study.entity.Classification;

import java.util.List;

public interface ClassificationService {
    List<Classification> getClassificationList();
    void createClassification(Classification Classification);
    void deleteClassification(String id);

    void updateClassification(Classification Classification);
}
