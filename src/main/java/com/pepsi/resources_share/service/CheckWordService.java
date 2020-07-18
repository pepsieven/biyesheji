package com.pepsi.resources_share.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pepsi.resources_share.entity.CheckWord;

/**
 * @author pepsiL
 * @create 2020-04-01 2:21 下午
 */
public interface CheckWordService extends IService<CheckWord> {
    CheckWord getOne(String response);
}
