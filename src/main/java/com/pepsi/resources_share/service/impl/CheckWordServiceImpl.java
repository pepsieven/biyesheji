package com.pepsi.resources_share.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pepsi.resources_share.entity.CheckWord;
import com.pepsi.resources_share.mapper.CheckWordMapper;
import com.pepsi.resources_share.service.CheckWordService;
import org.springframework.stereotype.Service;

/**
 * @author pepsiL
 * @create 2020-04-01 2:21 下午
 */
@Service
public class CheckWordServiceImpl extends ServiceImpl<CheckWordMapper, CheckWord> implements CheckWordService {
    @Override
    public CheckWord getOne(String response) {
        return baseMapper.selectOne(Wrappers.<CheckWord>lambdaQuery().eq(CheckWord::getResourceId,response));
    }
}
