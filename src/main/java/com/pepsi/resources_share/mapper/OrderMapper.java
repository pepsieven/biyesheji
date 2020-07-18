package com.pepsi.resources_share.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pepsi.resources_share.entity.Order;
import org.apache.ibatis.annotations.Mapper;

import java.util.stream.BaseStream;

/**
 * @author pepsiL
 * @create 2020-03-01 11:30 上午
 */
@Mapper
public interface OrderMapper extends BaseMapper<Order> {
}
