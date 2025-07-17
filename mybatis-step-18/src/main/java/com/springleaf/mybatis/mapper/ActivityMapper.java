package com.springleaf.mybatis.mapper;

import com.springleaf.mybatis.annotations.Delete;
import com.springleaf.mybatis.po.Activity;

import java.util.List;

public interface ActivityMapper {

    Activity queryActivityById(Activity activity);

    int insertActivity(Activity activity);

    @Delete("delete from activity where activity_id = #{activityId}")
    int deleteByActivityIdInt(Long activityId);

    List<Activity> listByActivityName(String activityName);

}