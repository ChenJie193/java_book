package com.c.study.service;


import com.c.study.entity.Banner;

import java.util.List;

public interface BannerService {
    List<Banner> getBannerList();
    void createBanner(Banner banner);
    void deleteBanner(String id);

    void updateBanner(Banner banner);
}
