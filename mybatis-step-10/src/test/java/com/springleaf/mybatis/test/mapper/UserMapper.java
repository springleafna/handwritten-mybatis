package com.springleaf.mybatis.test.mapper;

import com.springleaf.mybatis.test.po.User;

import java.util.List;

public interface UserMapper {

    User queryUserInfoById(Long id);

    User queryUserInfo(User req);

    List<User> queryUserInfoList();

    int updateUserInfo(User req);

    void insertUserInfo(User req);

    int deleteUserInfoByUserId(String userId);
}
