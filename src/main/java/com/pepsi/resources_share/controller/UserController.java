package com.pepsi.resources_share.controller;


import com.pepsi.resources_share.config.JwtTokenUtils;
import com.pepsi.resources_share.emuns.Role;
import com.pepsi.resources_share.entity.CurrentUser;
import com.pepsi.resources_share.entity.User;
import com.pepsi.resources_share.entity.UserBalance;
import com.pepsi.resources_share.entity.jwt.JwtRequest;
import com.pepsi.resources_share.entity.jwt.JwtResponse;
import com.pepsi.resources_share.service.UserBalanceService;
import com.pepsi.resources_share.service.UserIncomeService;
import com.pepsi.resources_share.service.UserOutlayService;
import com.pepsi.resources_share.service.UserService;
import com.pepsi.resources_share.utils.SecurityUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author pepsi
 * @since 2020-02-27
 */
@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    private final JwtTokenUtils jwtTokenUtils;

    private final AuthenticationManager authenticationManager;

    private final UserBalanceService userBalanceService;

    private final UserOutlayService userOutlayService;

    private final UserIncomeService userIncomeService;

    public UserController(UserService userService, JwtTokenUtils jwtTokenUtils, AuthenticationManager authenticationManager, UserBalanceService userBalanceService, UserOutlayService userOutlayService, UserIncomeService userIncomeService) {
        this.userService = userService;
        this.jwtTokenUtils = jwtTokenUtils;
        this.authenticationManager = authenticationManager;
        this.userBalanceService = userBalanceService;
        this.userOutlayService = userOutlayService;
        this.userIncomeService = userIncomeService;
    }

    /**
     * 用户登陆（用spring security进行登陆拦截和权限认证）
     * 创作者用户的每个操作要加上注解@PreAuthorize("hasAuthority('2')")
     * 默认普通用户的其他操作全部开放不需认证
     * 登陆后前端获得这个token存进localstorage里进行保存，后台设置了有效时间为5天失效，只要不主动退出用户，皆可自动登陆
     *（不知道为什么加上了@RequestBody就出现415错误...）
     * @return User
     */
    @PostMapping("/login")
    public JwtResponse loginUser(JwtRequest request) {
        //1、使用spring security进行用户名和密码的加密匹对
        String username = request.getUsername();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, request.getPassword()));

        //2、将其使用jwt方法进行装载去token放进header头
        String token = jwtTokenUtils.generateToken(username);
        return new JwtResponse(token);
    }

    /**
     * 取得已经登陆的用户信息，返回给前台存进localstorage
     *
     * @return
     */
    @GetMapping("/g")
    public User getLoginUser() {
        CurrentUser currentUser = SecurityUtils.getCurrentUser();
        return userService.getById(currentUser.getId());

    }

    /**
     * 注册新用户
     * @return
     */
    @PostMapping("/reg")
    public Object register(User user){
        user.setRole(Role.NON_CREATOR.getValue());
        return userService.register(user);
    }

    /**
     * 测试
     *
     * @return
     */
    @PutMapping("/test")
    @Transactional(propagation = Propagation.REQUIRED)
    public int test(User user) {
        return userService.updateUser(user);
    }


    /**
     * 管理员分页查看所有用户
     *
     * @return
     */
    @PostMapping("/All")
    public List<User> getAll() {
        return userService.list();
    }

    /**
     * 申请认证作者的用户
     * @return
     */
    @PostMapping("/gVer")
    public List<User> getVerify() {
        return userService.getApplyAuthor();
    }

    /**
     * 通过作者认证
     * @param id
     * @return
     */
    @PostMapping("/cApply")
    public Object confirmVerify(@RequestParam int id){
        return userService.confirmApply(id);
    }

    /**
     * 不通过作者认证
     * @param id
     * @return
     */
    @PostMapping("/rApply")
    public Object rejectVerify(@RequestParam int id){
        return userService.rejectApply(id);
    }


    /**
     * 更新单个用户(管理员管理用户/用户自身的更新密码或用户名操作皆可调用此方法)
     *
     * @param user
     * @return
     */
    @PutMapping("/u")
    public int updateOne( User user) {
        return userService.updateUser(user);
    }

    /**
     * 注册用户/管理员新加用户(要为余额表也新添加记录)
     * 插入方法
     *
     * @param user
     * @return
     */
    @PostMapping("/i")
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean insertOne(@RequestBody User user) {
        //  插入用户并返回数据库自动增长对id
        int userid = userService.register(user);
        //  给user_balance表插入记录
        UserBalance userBalance = new UserBalance();
        userBalance.setUserId(userid);
        boolean b = userBalanceService.save(userBalance);
        return true;
    }

    /**
     * 管理员删除一个用户(余额表和收入支出表也要跟着被删除)
     * 软删除，mybatisplus会自动将数据库中disabled字段变成1（删除状态）
     *
     * @return
     */
    @DeleteMapping("/r")
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean removeOne(@RequestBody Integer id) {
        userService.removeById(id);
        userBalanceService.removeBalanceByUserid(id);
        userIncomeService.removeByUserid(id);
        userOutlayService.removeByUserid(id);
        return true;
    }

    /**
     * 打开资源页面的时候获取作者信息的方法
     * @param userId
     * @return
     */
    @PostMapping("/aut")
    public User getAuthor(@RequestParam int userId){
        System.out.println("--------"+userId);
        return userService.getById(userId);
    }


    /**
     * 用户提交申请认证为作者开放上传资源权限
     * @return
     */
    @PostMapping("/AC")
    public Boolean  submitApprove(User user){
        CurrentUser currentUser = SecurityUtils.getCurrentUser();
        user.setId(currentUser.getId());
        user.setVerifyStatus(Role.NON_CREATOR.getValue());
        return userService.updateById(user);
    }

    /**
     * 检查用户提交了申请认证没有，避免重复提交
     * "1"是待审核
     * "2"是已经是作者
     * "3"是未提交的
     * @return
     */
    @PostMapping("/cSV")
    public Object checkSubmit(){
        CurrentUser currentUser = SecurityUtils.getCurrentUser();
        User byId = userService.getById(currentUser.getId());
        if("1".equals(byId.getVerifyStatus())){
            return Role.NON_CREATOR.getValue();
        }else if("2".equals(byId.getVerifyStatus())){
            return Role.CREATOR.getValue();
        }
        return "3";
    }


    /**
     *
     * @param newpassword
     * @return
     */
    @PostMapping("/cPass")
    public Object changePassword(@RequestParam String newpassword){

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String newencode = passwordEncoder.encode(newpassword.trim());


        CurrentUser currentUser = SecurityUtils.getCurrentUser();

        User user = new User();
        user.setPassword(newencode);
        user.setId(currentUser.getId());
        return userService.updateById(user);
    }

}
