package com.pepsi.resources_share.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pepsi.resources_share.entity.ResourceScore;

/**
 * @author pepsiL
 * @create 2020-05-07 9:12 上午
 */
public interface ResourceScoreService extends IService<ResourceScore> {

    Object confirmScore(ResourceScore resourceScore);

    int returnScore(int uid,String response);

}
