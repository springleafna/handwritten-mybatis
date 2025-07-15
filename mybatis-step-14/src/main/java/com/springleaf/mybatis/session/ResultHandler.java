package com.springleaf.mybatis.session;

import com.springleaf.mybatis.session.ResultContext;

/**
 * 结果处理器
 */
public interface ResultHandler {

    /**
     * 处理结果
     */
    void handleResult(ResultContext context);
}
