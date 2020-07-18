package com.pepsi.resources_share.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pepsi.resources_share.entity.ResourceScore;
import com.pepsi.resources_share.mapper.ResourceScoreMapper;
import com.pepsi.resources_share.service.ResourceScoreService;
import org.springframework.stereotype.Service;

/**
 * @author pepsiL
 * @create 2020-05-07 9:13 上午
 */
@Service
public class ResourceScoreServiceImpl extends ServiceImpl<ResourceScoreMapper, ResourceScore> implements ResourceScoreService {
    @Override
    public Object confirmScore(ResourceScore resourceScore) {
        return baseMapper.insert(resourceScore);
    }

    @Override
    public int returnScore(int uid, String response) {
        return baseMapper.selectOne(
                Wrappers.
                <ResourceScore>lambdaQuery().
                eq(ResourceScore::getUid, uid).
                eq(ResourceScore::getResponse, response)).getScore();
    }
}
