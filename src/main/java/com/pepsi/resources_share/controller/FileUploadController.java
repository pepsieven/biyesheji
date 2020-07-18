package com.pepsi.resources_share.controller;

import com.aliyun.oss.model.OSSObjectSummary;
import com.pepsi.resources_share.entity.CurrentUser;
import com.pepsi.resources_share.entity.FileUploadResult;
import com.pepsi.resources_share.service.FileUploadService;
import com.pepsi.resources_share.service.ReportResourceService;
import com.pepsi.resources_share.service.UserBalanceService;
import com.pepsi.resources_share.service.UserOutlayService;
import com.pepsi.resources_share.utils.ItemCF;
import com.pepsi.resources_share.utils.SecurityUtils;
import com.pepsi.resources_share.utils.SensitivewordFilter;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author pepsiL
 * @create 2020-03-04 1:48 下午
 */
@RestController
@RequestMapping("/file")
public class FileUploadController {

    private final FileUploadService fileUploadService;

    private final UserBalanceService userBalanceService;

    private final UserOutlayService userOutlayService;

    private final ReportResourceService reportResourceService;

    public FileUploadController(FileUploadService fileUploadService, UserBalanceService userBalanceService, UserOutlayService userOutlayService, ReportResourceService reportResourceService) {
        this.fileUploadService = fileUploadService;
        this.userBalanceService = userBalanceService;
        this.userOutlayService = userOutlayService;
        this.reportResourceService = reportResourceService;
    }


    /**
     * 上传到oss
     *
     * @return
     */
    @PostMapping("/p")
    @Transactional(propagation = Propagation.REQUIRED)
    public String upload(@RequestParam MultipartFile[] uploadFile, MultipartFile imgFile, FileUploadResult fileUploadResult) throws IOException {

        CurrentUser currentUser = SecurityUtils.getCurrentUser();
        fileUploadResult.setUserId(currentUser.getId());

        // 存入预览图

        byte[] images = imgFile.getBytes();
        fileUploadResult.setImages(images);


        for (MultipartFile file : uploadFile) {
            fileUploadService.upload(file, fileUploadResult);
        }

        return "ok";
    }

    /**
     * 上传到oss 进度条显示
     *
     * @return
     */
    @PostMapping("/tUR")
    @Transactional(propagation = Propagation.REQUIRED)
    public String testUploadPercent(@RequestParam MultipartFile[] uploadFile, HttpServletRequest request, MultipartFile imgFile, FileUploadResult fileUploadResult) throws IOException {

        CurrentUser currentUser = SecurityUtils.getCurrentUser();
        fileUploadResult.setUserId(currentUser.getId());

        String check = fileUploadResult.getResourceName() + fileUploadResult.getResourceDesc();
        SensitivewordFilter filter = new SensitivewordFilter();
        Set<String> set = filter.getSensitiveWord(check, 1);

        String result = "";
        if (set.size() != 0) {
            for (String str : set) {
                result += str + ",";
            }
            return result;
        }

        // 存入预览图
        byte[] images = imgFile.getBytes();
        fileUploadResult.setImages(images);

        for (MultipartFile file : uploadFile) {
            fileUploadService.upload2(file, request.getSession(), fileUploadResult);
        }

        return "ok";
    }

    /**
     * 获取实时长传进度
     *
     * @param request
     * @return
     */
    @RequestMapping("/percent")
    public int getUploadPercent(HttpServletRequest request) {
        HttpSession session = request.getSession();
        int percent = session.getAttribute("upload_percent") == null ? 0 : (int) session.getAttribute("upload_percent");
        System.out.println("session得到的值得==========" + percent);
        return percent;
    }


    /**
     * 拿一个图片
     *
     * @param response
     * @return
     */
    @PostMapping("/goi")
    public byte[] getOneImg(String response) {
        FileUploadResult oneFile = fileUploadService.getShow(response);
        if (oneFile != null) {
            return oneFile.getImages();
        }
        return null;
    }


    /**
     * 展示单个资源信息
     *
     * @param response
     * @return
     */
    @PostMapping("/fo")
    public FileUploadResult findByOss(@RequestParam String response) {
        return fileUploadService.getShow(response);
    }


    /**
     * 动态显示后台图片
     *
     * @param response
     * @param resourceName
     * @throws IOException
     */
    @RequestMapping("/testShowImg")
    public void testShowImg(HttpServletResponse response, @RequestParam String resourceName) throws IOException {
        FileUploadResult oneFile = fileUploadService.getOneFile(resourceName);
        response.setContentType("image/jpeg");
        response.setCharacterEncoding("UTF-8");
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(oneFile.getImages());
        outputStream.flush();
    }

    /**
     * 根据物品的协同过滤算法获得的推荐列表
     * @param
     * @return
     * @throws Exception
     */
    @PostMapping("/gPF")
    public List<FileUploadResult> getPerferFile() throws Exception {
        CurrentUser currentUser = SecurityUtils.getCurrentUser();
        ItemCF itemCF = new ItemCF();
        List<FileUploadResult> fileUploadResults = new ArrayList<>();
        List<RecommendedItem> recommendedItemsList = itemCF.personalPerfer(currentUser.getId());
        for (RecommendedItem items : recommendedItemsList) {
            System.out.printf("(%s,%f)", items.getItemID(), items.getValue());
            FileUploadResult show = fileUploadService.getShow(String.valueOf(items.getItemID()));
            fileUploadResults.add(show);
        }
        return fileUploadResults;
    }

    /**
     * 测试返回照片(只能显示一张)
     */
    @RequestMapping("/testImage")
    public byte[] testImage(HttpServletResponse response, @RequestParam String resourceName) throws IOException {
        FileUploadResult oneFile = fileUploadService.getOneFile(resourceName);
        if (oneFile.getImages() != null) {
            return oneFile.getImages();
        }
        return null;
    }

    /**
     * 列出资源列表
     *
     * @param
     * @return
     */
    @PostMapping("/im")
    public List<FileUploadResult> showImage(@RequestParam String resourceClass) {
        return fileUploadService.listPass(resourceClass);
    }

    /**
     * 根据文件名删除oss上的文件
     *
     * @param name
     * @return
     */
    @DeleteMapping
    public FileUploadResult delete(@RequestParam String name) {
        return fileUploadService.delete(name);
    }

    /**
     * 查询oss上的所有文件
     *
     * @return
     */
    @GetMapping("/list")
    public List<OSSObjectSummary> list() {
        return this.fileUploadService.listAll();
    }

    /**
     * 下载
     *
     * @param
     * @param response
     * @throws Exception
     */
    @GetMapping("/dl")
    public void download(@RequestParam String name, HttpServletResponse response) throws Exception {


        //通知浏览器以附件形式下载
        response.setHeader("Content-Disposition",
                "attachment;filename=" + new String(name.getBytes(), StandardCharsets.ISO_8859_1));
        fileUploadService.exportOssFile(response.getOutputStream(), name);

    }

    /**
     * 用户输入相关查询信息，后台通过模糊查询返回结果集
     *
     * @param resourceName
     * @return
     */
    @PostMapping("/sr")
    public List<FileUploadResult> fuzzyQuery(@RequestParam String resourceName) {
        return fileUploadService.fuzzyQuery(resourceName);
    }

    /**
     * 用户输入相关查询信息，后台通过模糊查询返回结果集
     *
     * @param classNum
     * @return
     */
    @PostMapping("/srC")
    public List<FileUploadResult> classImg(@RequestParam String classNum) {
        return fileUploadService.listClass(classNum);
    }

    /**
     * 用户查看自己上传的所有作品记录
     *
     * @param userId
     * @return
     */
    @PostMapping("/uar")
    public List<FileUploadResult> myResource(@RequestParam int userId) {
        return fileUploadService.myResource(userId);
    }

    /**
     * 用户下架资源
     *
     * @param fileUploadResult
     * @return
     */
    @PostMapping("/cR")
    public Object cancelResource(FileUploadResult fileUploadResult) {
        return fileUploadService.cancelUpload(fileUploadResult);
    }

    /**
     * 管理员根据状态查看文件记录条数
     *
     * @return
     */
    @PostMapping("/gCou")
    public Object getCounts(@RequestParam String passKey) {
        return fileUploadService.countResource(passKey);
    }

    /**
     * 管理时查看需要管理资源列表
     *
     * @param passKey
     * @return
     */
    @PostMapping("/gFBPk")
    public List<FileUploadResult> getFileByPasskey(@RequestParam String passKey) {
        return fileUploadService.getFileByPasskey(passKey);
    }

    /**
     * 管理员通过资源上传的申请
     *
     * @param response
     * @return
     */
    @PostMapping("/confirmResourceUpload")
    public Object confirmResourceUpload(@RequestParam String response) {
        return fileUploadService.confirmResourceUpload(response);
    }

    /**
     * 管理员拒绝资源上传的申请
     *
     * @param response
     * @return
     */
    @PostMapping("/rejectResourceUpload")
    public Object rejectResourceUpload(@RequestParam String response) {
        return fileUploadService.rejectResourceUpload(response);
    }

    /**
     * 用户举报违规资源
     * @param response
     * @return
     */
    @PostMapping("/reportResource")
    public Object reportResource(@RequestParam String response,@RequestParam int uid,@RequestParam String reportReason){
        int report = reportResourceService.report(response, uid, reportReason);
        if(report != 1){
            return false;
        }
        else {
            return fileUploadService.reportResource(response);
        }
    }

    /**
     * 管理员查看所有资源列表
     * @return
     */
    @PostMapping("/lAll")
    public List<FileUploadResult> listAllResource(){
        return fileUploadService.getAllResource();
    }




}
