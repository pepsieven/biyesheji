package com.pepsi.resources_share.controller;

import com.pepsi.resources_share.entity.CheckWord;
import com.pepsi.resources_share.service.CheckWordService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author pepsiL
 * @create 2020-04-01 3:12 下午
 */
@RestController
@RequestMapping("/checkword")
public class CheckWordController {

    private final CheckWordService checkWordService;

    public CheckWordController(CheckWordService checkWordService) {
        this.checkWordService = checkWordService;
    }

    @PostMapping("/g")
    public CheckWord getOne(@RequestParam String response){
        return checkWordService.getOne(response);
    }

}
