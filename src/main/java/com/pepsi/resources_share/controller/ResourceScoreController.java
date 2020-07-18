package com.pepsi.resources_share.controller;

import com.pepsi.resources_share.entity.CurrentUser;
import com.pepsi.resources_share.entity.ResourceScore;
import com.pepsi.resources_share.service.ResourceScoreService;
import com.pepsi.resources_share.utils.SecurityUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author pepsiL
 * @create 2020-05-07 10:53 上午
 */
@RestController
@RequestMapping("/ResourceScore")
public class ResourceScoreController {
    private final ResourceScoreService resourceScoreService;

    public ResourceScoreController(ResourceScoreService resourceScoreService) {
        this.resourceScoreService = resourceScoreService;
    }


    /**
     * 确认用户是否进行评分
     * @param resourceScore
     * @return
     */
    @RequestMapping("/cfs")
    public Object ConfirmScore(ResourceScore resourceScore){
        CurrentUser currentUser = SecurityUtils.getCurrentUser();
        resourceScore.setUid(currentUser.getId());
        return resourceScoreService.confirmScore(resourceScore);
    }

    /**
     * 返回该用户对资源的打分
     * @return
     */
    @RequestMapping("/res")
    public int ReturnScore(@RequestParam String response){
        CurrentUser currentUser = SecurityUtils.getCurrentUser();
        return resourceScoreService.returnScore(currentUser.getId(),response);
    }

}
