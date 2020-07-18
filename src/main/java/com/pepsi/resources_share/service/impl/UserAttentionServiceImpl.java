package com.pepsi.resources_share.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pepsi.resources_share.entity.UserAttention;
import com.pepsi.resources_share.mapper.UserAttentionMapper;
import com.pepsi.resources_share.service.UserAttentionService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author pepsiL
 * @create 2020-03-18 7:44 下午
 */
@Service
public class UserAttentionServiceImpl extends ServiceImpl<UserAttentionMapper, UserAttention> implements UserAttentionService {

    /**
     * 用户关注一个作者使用的方法
     * @param userAttention
     * @return
     */
    @Override
    public Object attentionAuthor(UserAttention userAttention) {
        return baseMapper.insert(userAttention);
    }

    /**
     * 用户取消关注一个作者
     */
    @Override
    public Object cancelAttention(UserAttention userAttention) {
        return baseMapper.delete(Wrappers.<UserAttention>lambdaUpdate().
                eq(UserAttention::getAuthorId,userAttention.getAuthorId()).
                eq(UserAttention::getUserId,userAttention.getUserId()));
    }

    @Override
    public boolean checkAttention(UserAttention userAttention) {
        return baseMapper.selectOne(Wrappers.<UserAttention>lambdaQuery().
                eq(UserAttention::getAuthorId,userAttention.getAuthorId()).
                eq(UserAttention::getUserId,userAttention.getUserId()))!=null?true:false;
    }

    @Override
    public List<UserAttention> getAttentionList(int userid) {
        return baseMapper.selectList(Wrappers.<UserAttention>lambdaQuery().eq(UserAttention::getUserId,userid));
    }


}
