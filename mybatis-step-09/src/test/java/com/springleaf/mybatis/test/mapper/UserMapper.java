package com.springleaf.mybatis.test.mapper;

import com.springleaf.mybatis.test.po.User;

public interface UserMapper {

    User queryUserInfoById(Long uId);

    User queryUserInfo(User req);
}
