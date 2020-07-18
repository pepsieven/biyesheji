package com.pepsi.resources_share.mapper;

import com.pepsi.resources_share.entity.UserBalance;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

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
@Repository
public interface UserBalanceMapper extends BaseMapper<UserBalance> {

    /**
     * alipay调起时使用调方法
     * @param balance
     * @param userid
     * @return
     */
    @Update("UPDATE user_balance set balance = balance+#{balance} where user_id = #{userid}")
    int updateBalance(@Param("balance")BigDecimal balance,@Param("userid") int userid);

    /**
     * 消费者购买作品时使用的方法
     * @param price
     * @param userid
     * @return
     */
    @Update("UPDATE user_balance set balance = balance-#{price},total_outlay = total_outlay+#{price} where user_id = #{userid}")
    int updateConsumerBalance(@Param("price")BigDecimal price,@Param("userid") int userid);

    /**
     * 作者作品被消费后使用的方法
     * @param price
     * @param userid
     * @return
     */
    @Update("UPDATE user_balance set balance = balance+#{price},total_income = total_income+#{price} where user_id = #{userid}")
    int updateAuthorBalance(@Param("price")BigDecimal price,@Param("userid") int userid);

}
