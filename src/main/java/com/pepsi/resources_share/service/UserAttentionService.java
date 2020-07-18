package com.pepsi.resources_share.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pepsi.resources_share.entity.UserAttention;

import java.util.List;

/**
 * @author pepsiL
 * @create 2020-03-18 7:43 下午
 */
public interface UserAttentionService extends IService<UserAttention> {

    /**
     * 关注作者
     * @param userAttention
     * @return
     */
    Object attentionAuthor(UserAttention userAttention);

    Object cancelAttention(UserAttention userAttention);


    boolean checkAttention(UserAttention userAttention);

    List<UserAttention> getAttentionList(int userid);

}
