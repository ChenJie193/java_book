package com.c.study.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.c.study.entity.ThingWish;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ThingWishMapper extends BaseMapper<ThingWish> {

    List<Map> getThingWishList(String userId);
}
