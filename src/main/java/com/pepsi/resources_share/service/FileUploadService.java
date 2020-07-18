package com.pepsi.resources_share.service;

import com.aliyun.oss.model.OSSObjectSummary;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pepsi.resources_share.entity.FileUploadResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.OutputStream;
import java.util.List;

/**
 * @author pepsiL
 * @create 2020-03-04 1:04 下午
 */
public interface FileUploadService extends IService<FileUploadResult> {



    FileUploadResult findPreviewResult(String resourceName);

    /**
     * 根据文件类型取出对应的文件
     * @return
     */
    List<FileUploadResult> listPass(String resourceClass);

    /**
     * 根据文件类型取出对应的文件
     * @return
     */
    List<FileUploadResult> listClass(String classNum);

    /**
     * 用户下架资源的方法
     * @return
     */
    Object cancelUpload(FileUploadResult fileUploadResult);

    FileUploadResult getByName(String name);

    /**
     * 用户查找关键字搜索的资源列表
     *
     * @param resourceName
     * @return
     */
    List<FileUploadResult> fuzzyQuery(String resourceName);

    /**
     * 列出用户自己上传的作品
     *
     * @param userId
     * @return
     */
    List<FileUploadResult> myResource(int userId);


    /**
     * 存储预览图片对象
     */
    FileUploadResult getOneFile(String resourceNaem);

    /**
     * 判断文件类型长度
     */
    Boolean judgeFile(MultipartFile file);

    /**
     * 文件上传
     *
     * @param uploadFile
     * @return
     */
    FileUploadResult upload(MultipartFile uploadFile, FileUploadResult f);

    /**
     * 文件上传,测试进度条
     *
     * @param uploadFile
     * @return
     */
    FileUploadResult upload2(MultipartFile uploadFile, HttpSession session, FileUploadResult f);

    /**
     * 生产文件路径和文件名 例如：//images/2020/03/02/1232334253.jpg
     *
     * @param sourceFileName
     * @return
     */
    String getFilePath(String sourceFileName);

    /**
     * 查看文件列表
     *
     * @return
     */
    List<OSSObjectSummary> listAll();

    /**
     * 删除文件
     *
     * @param objectName
     * @return
     */
    FileUploadResult delete(String objectName);

    /**
     * 下载文件
     *
     * @param os
     * @param objectName
     */
    void exportOssFile(OutputStream os, String objectName) throws Exception;

    /**
     * 根据oss的编号进行单一查找记录
     * 用户查看订单的时候，点开已购买的资源信息
     *
     * @param response
     * @return
     */
    FileUploadResult getShow(String response);

    Object countResource(String passKey);

    List<FileUploadResult> getFileByPasskey(String passKey);

    Object confirmResourceUpload(String response);

    Object rejectResourceUpload(String response);

    Object reportResource(String resourceId);

    List<FileUploadResult> getAllResource();

}
