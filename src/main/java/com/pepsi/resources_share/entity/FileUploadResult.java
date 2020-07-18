package com.pepsi.resources_share.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 资源类
 * @author pepsiL
 * @create 2020-03-04 12:59 下午
 */

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("resources")
public class FileUploadResult {
    private static final long serialVersionUID = 1L;

    private Integer id;

    private String name;

    private String resourceName;

    private String status;

    private Integer userId;

    private String response;

    private Boolean disabled;

    private String resourceType;

    private String needCharge;

    private String resourceId;

    private String resourceDesc;

    private BigDecimal resourcePrice;

    private String resourceClass;

    private String passKey;

    private  String classNum;

    private byte[] images;

    private String report;

}
