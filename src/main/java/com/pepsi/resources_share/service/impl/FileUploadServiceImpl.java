package com.pepsi.resources_share.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.event.ProgressEvent;
import com.aliyun.oss.event.ProgressEventType;
import com.aliyun.oss.event.ProgressListener;
import com.aliyun.oss.model.*;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pepsi.resources_share.config.AliyunConfig;
import com.pepsi.resources_share.emuns.AuditStatus;
import com.pepsi.resources_share.entity.CheckWord;
import com.pepsi.resources_share.entity.FileUploadResult;
import com.pepsi.resources_share.mapper.CheckWordMapper;
import com.pepsi.resources_share.mapper.FileUploadResultMapper;
import com.pepsi.resources_share.service.FileUploadService;
import com.pepsi.resources_share.utils.FileSizeUtil;
import com.pepsi.resources_share.utils.ParseText;
import com.pepsi.resources_share.utils.SensitivewordFilter;
import lombok.SneakyThrows;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.List;
import java.util.Set;

/**
 * @author pepsiL
 * @create 2020-03-04 1:08 下午
 */
@Service
public class FileUploadServiceImpl extends ServiceImpl<FileUploadResultMapper, FileUploadResult> implements FileUploadService {
    // 允许上传的格式
    private static final String[] IMAGE_TYPE = new String[]{
            ".bmp", ".jpg", ".jpeg", ".gif", ".png", ".psd", ".doc", ".ppt", ".docx", ".rar", ".zip", ".pptx", ".pdf"
    };
    @Resource
    FileUploadResultMapper fileUploadResultMapper;
    @Resource
    CheckWordMapper checkWordMapper;
    @Autowired
    private OSS ossClient;
    @Autowired
    private AliyunConfig aliyunConfig;

    private final static Logger logger = LoggerFactory.getLogger(FileUploadService.class);


    /**
     * 查找预览图先存好的数据库记录
     *
     * @param resourceName
     * @return
     */
    @Override
    public FileUploadResult findPreviewResult(String resourceName) {
        return baseMapper.selectOne(Wrappers.<FileUploadResult>lambdaQuery().eq(FileUploadResult::getResourceName, resourceName));
    }

    /**
     * 首页列出通过审核的资源
     *
     * @return
     */
    @Override
    public List<FileUploadResult> listPass(String resourceClass) {
        return baseMapper.selectList(Wrappers.<FileUploadResult>lambdaQuery().eq(FileUploadResult::getPassKey, AuditStatus.PASS_RESOURCE.getValue()).eq(FileUploadResult::getResourceClass, resourceClass));
    }

    @Override
    public List<FileUploadResult> listClass(String classNum) {
        return baseMapper.selectList(Wrappers.<FileUploadResult>lambdaQuery().eq(FileUploadResult::getPassKey, AuditStatus.PASS_RESOURCE.getValue()).eq(FileUploadResult::getClassNum, classNum));
    }

    @Override
    public Object cancelUpload(FileUploadResult fileUploadResult) {
        fileUploadResult.setPassKey(AuditStatus.CANCEL_UPLOAD.getValue());
        return baseMapper.update(fileUploadResult, Wrappers.<FileUploadResult>lambdaUpdate().eq(FileUploadResult::getResponse, fileUploadResult.getResponse()));
    }

    @Override
    public FileUploadResult getByName(String name) {
        return baseMapper.selectOne(Wrappers.<FileUploadResult>lambdaQuery().eq(FileUploadResult::getName, name));
    }

    /**
     * 用户通过关键字查询相关的资源列表并展示出来
     *
     * @param resourceName
     * @return
     */
    @Override
    public List<FileUploadResult> fuzzyQuery(String resourceName) {
        return baseMapper.selectList(Wrappers.<FileUploadResult>lambdaQuery().like(FileUploadResult::getResourceName, resourceName));
    }

    @Override
    public List<FileUploadResult> myResource(int userId) {
        return baseMapper.selectList(Wrappers.<FileUploadResult>lambdaQuery().eq(FileUploadResult::getUserId, userId));
    }


    @Override
    public FileUploadResult getOneFile(String resourceNaem) {
        return baseMapper.selectOne(Wrappers.<FileUploadResult>lambdaQuery().eq(FileUploadResult::getResourceName, resourceNaem));
    }


    /**
     * 判断上传文件是否为小于16mb图片的方法
     *
     * @param file
     * @return
     */
    @Override
    public Boolean judgeFile(MultipartFile file) {
        FileSizeUtil fileSizeUtil = new FileSizeUtil();
        String originalFilename = file.getOriginalFilename();
        if (originalFilename.contains("jpg") || originalFilename.contains("jpeg") || originalFilename.contains("png")) {
            long bytes = file.getSize();
            if (fileSizeUtil.checkFileSize(bytes, 16, "M")) {
                return true;
            }
        }
        return false;
    }

    /**
     * 文件上传
     *
     * @param uploadFile
     * @return
     */
    @Override
    public FileUploadResult upload(MultipartFile uploadFile, FileUploadResult f) {
        //校验格式
        boolean isLegal = false;
        for (String type : IMAGE_TYPE) {
            if (StringUtils.endsWithIgnoreCase(uploadFile.getOriginalFilename(),
                    type)) {
                isLegal = true;
                break;
            }
        }
        // 封装Result对象，并且将文件的byte数组放置到result对象中
        FileUploadResult fileUploadResult = new FileUploadResult();
        if (!isLegal) {
            fileUploadResult.setStatus("error");
            return fileUploadResult;
        }
        //文件新路径
        String fileName = uploadFile.getOriginalFilename();
        String filePath = getFilePath(fileName);
        //上传到阿里云
        try {
            ossClient.putObject(aliyunConfig.getBucketName(), filePath,
                    new ByteArrayInputStream(uploadFile.getBytes()));
        } catch (Exception e) {
            e.printStackTrace();
            //上传失败
            fileUploadResult.setStatus("error");
            return fileUploadResult;
        }
        fileUploadResult.setStatus("done");
        fileUploadResult.setResponse("success");
        //this.aliyunConfig.getUrlPrefix()
        fileUploadResult.setName(filePath);
        fileUploadResult.setResponse(String.valueOf(System.currentTimeMillis()));
        fileUploadResult.setUserId(f.getUserId());
        fileUploadResult.setResourceName(f.getResourceName());
        fileUploadResult.setResourceType(f.getResourceType());
        fileUploadResult.setNeedCharge(f.getNeedCharge());
        fileUploadResult.setResourceDesc(f.getResourceDesc());
        String resourceId = f.getResourceName() + f.getUserId();
        fileUploadResult.setResourceId(resourceId);
        fileUploadResult.setImages(f.getImages());
        fileUploadResultMapper.insert(fileUploadResult);
        return fileUploadResult;
    }

    /**
     * 文件上传 测试进度条显示
     *
     * @param uploadFile
     * @return
     */
    @SneakyThrows
    @Override
    public FileUploadResult upload2(MultipartFile uploadFile, HttpSession session, FileUploadResult f) {
        //校验格式
        boolean isLegal = false;
        for (String type : IMAGE_TYPE) {
            if (StringUtils.endsWithIgnoreCase(uploadFile.getOriginalFilename(),
                    type)) {
                isLegal = true;
                break;
            }
        }

        //检测文件中的文字内容是否有敏感词汇并将此进行数据库事务逻辑处理
        SensitivewordFilter filter = new SensitivewordFilter();
        String s = StringUtils.substringAfterLast(uploadFile.getOriginalFilename(), ".");
        Set<String> set = filter.getSensitiveWord(ParseText.parse(uploadFile.getBytes(), s), 1);
        String result = "";
        for (String str : set) {
            result += str + ",";
        }


        File f1 = null;
        try {
            f1 = File.createTempFile("tmp", null);
            uploadFile.transferTo(f1);
            f1.deleteOnExit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 封装Result对象，并且将文件的byte数组放置到result对象中
        FileUploadResult fileUploadResult = new FileUploadResult();
        if (!isLegal) {
            fileUploadResult.setStatus("error");
            return fileUploadResult;
        }
        //文件新路径
        String fileName = uploadFile.getOriginalFilename();
        String filePath = getFilePath(fileName);
        //上传到阿里云
        try {
            //进度条上传
            ossClient.putObject(new PutObjectRequest(aliyunConfig.getBucketName(), filePath, f1)
                    .withProgressListener(new PutObjectProgressListener(session)));
        } catch (Exception e) {
            e.printStackTrace();
            //上传失败
            fileUploadResult.setStatus("error");
            return fileUploadResult;
        }


        // 上传完服务器后，对数据库对事务逻辑处理
        fileUploadResult.setStatus("done");
        fileUploadResult.setName(filePath);
        fileUploadResult.setResponse(String.valueOf(System.currentTimeMillis()));
        fileUploadResult.setUserId(f.getUserId());
        fileUploadResult.setResourceName(f.getResourceName());
        fileUploadResult.setResourceType(f.getResourceType());
        fileUploadResult.setNeedCharge(f.getNeedCharge());
        fileUploadResult.setResourceDesc(f.getResourceDesc());
        String resourceId = f.getResourceName() + f.getUserId();
        fileUploadResult.setResourceId(resourceId);
        fileUploadResult.setImages(f.getImages());
        fileUploadResultMapper.insert(fileUploadResult);

        // 敏感词事务处理
        CheckWord checkWord = new CheckWord();
        checkWord.setIllegalNum(set.size());
        checkWord.setIllegalWord(result);
        checkWord.setResourceId(fileUploadResult.getResponse());
        checkWordMapper.insert(checkWord);

        return fileUploadResult;
    }

    public static class PutObjectProgressListener implements ProgressListener {

        private long bytesWritten = 0;
        private long totalBytes = -1;
        private boolean succeed = false;
        private HttpSession session;
        private int percent = 0;

        //构造方法中加入session
        public PutObjectProgressListener() {
        }

        public PutObjectProgressListener(HttpSession mSession) {
            this.session = mSession;
            session.setAttribute("upload_percent", percent);
        }

        @Override
        public void progressChanged(ProgressEvent progressEvent) {
            long bytes = progressEvent.getBytes();
            ProgressEventType eventType = progressEvent.getEventType();
            switch (eventType) {
                case TRANSFER_STARTED_EVENT:
                    System.out.println("Start to upload......");
                    break;
                case REQUEST_CONTENT_LENGTH_EVENT:
                    this.totalBytes = bytes;
                    System.out.println(this.totalBytes + " bytes in total will be uploaded to OSS");
                    break;
                case REQUEST_BYTE_TRANSFER_EVENT:
                    this.bytesWritten += bytes;
                    if (this.totalBytes != -1) {
                        int percent = (int) (this.bytesWritten * 100.0 / this.totalBytes);
                        //将进度percent放入session中
                        session.setAttribute("upload_percent", percent);
                        System.out.println(bytes + " bytes have been written at this time, upload progress: " + percent + "%(" + this.bytesWritten + "/" + this.totalBytes + ")");
                    } else {
                        System.out.println(bytes + " bytes have been written at this time, upload ratio: unknown" + "(" + this.bytesWritten + "/...)");
                    }
                    break;
                case TRANSFER_COMPLETED_EVENT:
                    this.succeed = true;
                    System.out.println("Succeed to upload, " + this.bytesWritten + " bytes have been transferred in total");
                    break;
                case TRANSFER_FAILED_EVENT:
                    System.out.println("Failed to upload, " + this.bytesWritten + " bytes have been transferred");
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 获得文件路径和名称
     *
     * @param sourceFileName
     * @return
     */
    @Override
    public String getFilePath(String sourceFileName) {
        DateTime dateTime = new DateTime();
        return "images/" + dateTime.toString("yyyy")
                + "/" + dateTime.toString("MM") + "/"
                + dateTime.toString("dd") + "/" + System.currentTimeMillis()
                + RandomUtils.nextInt(100, 9999) + "."
                + StringUtils.substringAfterLast(sourceFileName, ".");
    }

    /**
     * 查看所有列表
     *
     * @return
     */
    @Override
    public List<OSSObjectSummary> listAll() {
        // 设置最大个数。
        final int maxKeys = 200;
        // 列举文件。
        ObjectListing objectListing =
                ossClient.listObjects(new ListObjectsRequest
                        (aliyunConfig.getBucketName()).withMaxKeys(maxKeys));


        List<OSSObjectSummary> sums
                = objectListing.getObjectSummaries();
        return sums;
    }

    /**
     * 文件删除
     *
     * @param objectName
     * @return
     */
    @Override
    public FileUploadResult delete(String objectName) {
        // 根据BucketName,objectName删除文件
        ossClient.deleteObject(aliyunConfig.getBucketName(), objectName);
        FileUploadResult fileUploadResult = new FileUploadResult();
        fileUploadResult.setName(objectName);
        fileUploadResult.setStatus("removed");
        fileUploadResult.setResponse("success");
        return fileUploadResult;
    }

    /**
     * 文件下载
     *
     * @param os
     * @param objectName
     */
    @Override
    public void exportOssFile(OutputStream os, String objectName) throws Exception {
        // ossObject包含文件所在的存储空间名称、文件名称、文件元信息以及一个输入流。
        OSSObject ossObject = ossClient.getObject(aliyunConfig.getBucketName(), objectName);
        System.out.println("???????" + ossObject.getObjectContent());
        // 读取文件内容。
        BufferedInputStream in = new BufferedInputStream(ossObject.getObjectContent());
        BufferedOutputStream out = new BufferedOutputStream(os);
        byte[] buffer = new byte[1024];
        int lenght = 0;
        while ((lenght = in.read(buffer)) != -1) {
            out.write(buffer, 0, lenght);
        }
        if (out != null) {
            out.flush();
            out.close();
        }
        if (in != null) {
            in.close();
        }
    }

    @Override
    public FileUploadResult getShow(String response) {
        return baseMapper.selectOne(Wrappers.<FileUploadResult>lambdaQuery().eq(FileUploadResult::getResponse, response));
    }

    @Override
    public Object countResource(String passKey) {
        return baseMapper.selectCount(Wrappers.<FileUploadResult>lambdaQuery().eq(FileUploadResult::getPassKey, passKey));
    }

    @Override
    public List<FileUploadResult> getFileByPasskey(String passKey) {
        return baseMapper.selectList(Wrappers.<FileUploadResult>lambdaQuery().eq(FileUploadResult::getPassKey, passKey));
    }

    @Override
    public Object confirmResourceUpload(String response) {
        FileUploadResult fileUploadResult = new FileUploadResult();
        fileUploadResult.setPassKey(AuditStatus.PASS_RESOURCE.getValue());
        return baseMapper.update(fileUploadResult, Wrappers.<FileUploadResult>lambdaUpdate().eq(FileUploadResult::getResponse, response));
    }

    @Override
    public Object rejectResourceUpload(String response) {
        FileUploadResult fileUploadResult = new FileUploadResult();
        fileUploadResult.setPassKey(AuditStatus.NOT_PASS_RESOURCE.getValue());
        return baseMapper.update(fileUploadResult, Wrappers.<FileUploadResult>lambdaUpdate().eq(FileUploadResult::getResponse, response));
    }

    @Override
    public Object reportResource(String response) {
        FileUploadResult f = baseMapper.selectOne(Wrappers.<FileUploadResult>lambdaQuery().eq(FileUploadResult::getResponse, response));
        FileUploadResult fileUploadResult = new FileUploadResult();
        int s = Integer.parseInt(f.getReport()) + 1;
        String newReport = String.valueOf(s);
        fileUploadResult.setReport(newReport);
        return baseMapper.update(fileUploadResult, Wrappers.<FileUploadResult>lambdaUpdate().eq(FileUploadResult::getResponse, response));
    }

    @Override
    public List<FileUploadResult> getAllResource() {
        return baseMapper.selectList(Wrappers.<FileUploadResult>lambdaQuery().eq(FileUploadResult::getPassKey,"3"));
    }

}
