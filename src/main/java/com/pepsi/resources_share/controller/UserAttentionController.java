package com.pepsi.resources_share.controller;

import com.pepsi.resources_share.entity.CurrentUser;
import com.pepsi.resources_share.entity.UserAttention;
import com.pepsi.resources_share.service.UserAttentionService;
import com.pepsi.resources_share.utils.SecurityUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author pepsiL
 * @create 2020-03-18 7:45 下午
 */
@RestController
@RequestMapping("/userattention")
public class UserAttentionController {
    private  final UserAttentionService userAttentionService;

    public UserAttentionController(UserAttentionService userAttentionService) {
        this.userAttentionService = userAttentionService;
    }


    /**
     * 获得用户关注的作者列表
     * @param
     * @return
     */
    @PostMapping("/gAL")
    public List<UserAttention> getAttentionList(){
        CurrentUser currentUser = SecurityUtils.getCurrentUser();
        return userAttentionService.getAttentionList(currentUser.getId());
    }


    /**
     * 关注一个作者
     * @param userAttention
     * @return
     */
    @PostMapping("/i")
    public Object insertAttention(UserAttention userAttention){
        CurrentUser currentUser = SecurityUtils.getCurrentUser();
        userAttention.setUserId(currentUser.getId());
        userAttention.setUserName(currentUser.getUsername());

        if(!userAttentionService.checkAttention(userAttention)){
            return userAttentionService.attentionAuthor(userAttention);
        }
        return  null;
    }

    /**
     * 取消关注
     * @param userAttention
     * @return
     */
    @PostMapping("/caA")
    public Object cancelAttention(UserAttention userAttention){
        CurrentUser currentUser = SecurityUtils.getCurrentUser();
        userAttention.setUserId(currentUser.getId());
        userAttention.setUserName(currentUser.getUsername());
        return userAttentionService.cancelAttention(userAttention);
    }

    /**
     * 检测是否有关注记录
     * @param userAttention
     * @return
     */
    @PostMapping("/ca")
    public boolean checkAttention(UserAttention userAttention){
        CurrentUser currentUser = SecurityUtils.getCurrentUser();
        userAttention.setUserId(currentUser.getId());
        userAttention.setUserName(currentUser.getUsername());
        return userAttentionService.checkAttention(userAttention);
    }


}
