package com.pepsi.resources_share.mapper;

import com.pepsi.resources_share.entity.UserIncome;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author pepsi
 * @since 2020-02-27
 */
@Mapper
public interface UserIncomeMapper extends BaseMapper<UserIncome> {


}
