package com.pepsi.resources_share.controller;


import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Iterator;

/**
 * @author pepsiL
 * @create 2020-03-28 10:52 上午
 */
@RestController
@RequestMapping("/api/excel")
public class ExcelController {

    @PostMapping()
    public void mockPaper(@RequestParam("/file") MultipartFile file) {
        try {
            XWPFDocument xwpfDocument = new XWPFDocument(file.getInputStream());
            XWPFParagraph para;
            Iterator<XWPFParagraph> paragraphsIterator = xwpfDocument.getParagraphsIterator();
            while (paragraphsIterator.hasNext()) {
                para = paragraphsIterator.next();
                System.out.println("输出的文本:" + para.getText());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
