package com.pepsi.resources_share.service;

import com.pepsi.resources_share.entity.UserOutlay;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author pepsi
 * @since 2020-02-27
 */
public interface UserOutlayService extends IService<UserOutlay> {

    int removeByUserid(int userid);

    Boolean confirmPayStatus(int userid,int orderid);

}
