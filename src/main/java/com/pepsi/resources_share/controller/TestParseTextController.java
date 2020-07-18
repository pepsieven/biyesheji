package com.pepsi.resources_share.controller;

import com.pepsi.resources_share.utils.ParseText;
import com.pepsi.resources_share.utils.SensitivewordFilter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Set;

/**
 * @author pepsiL
 * @create 2020-03-30 11:40 上午
 */
@RestController
@RequestMapping("/testparse")
public class TestParseTextController {

    @PostMapping
    public Set<String> test(@RequestParam MultipartFile testFile) throws IOException {
        byte[] testByte = testFile.getBytes();
        String originalFilename = testFile.getOriginalFilename();
        String s = StringUtils.substringAfterLast(originalFilename, ".");
        SensitivewordFilter filter = new SensitivewordFilter();
        Set<String> set = filter.getSensitiveWord(ParseText.parse(testByte, s), 1);
        System.out.println("待检测语句字数：" + ParseText.parse(testByte, s).length());
        System.out.println("语句中包含敏感词的个数为：" + set.size() + "。包含：" + set);
        String result = "";
        if (set != null) {
            for (String str : set) {
                result += str + ",";
            }
            System.out.println(result);
            return set;
        } else {
            return null;
        }

    }

}
