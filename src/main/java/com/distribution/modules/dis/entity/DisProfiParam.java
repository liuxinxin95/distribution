package com.distribution.modules.dis.entity;

import lombok.Data;

/**
 * 分润设置实体类
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table dis_profi_param
 *
 * @mbg.generated do_not_delete_during_merge
 */
@Data
public class DisProfiParam {
    /** */
    private String id;

    /**
     * 平台id
     */
    private String disPlatformId;

    /**
     * 分润模型，如 百分比和固定金额
     */
    private String disProMode;

    /**
     * 分润类别，如上级发展下级分润 ，交易分润。。。。
     */
    private String disProType;

    /**
     * 分润值
     */
    private String disProValue;

    /**
     * 从下往上对应的级别关系
     */
    private String disProLevel;

    /**
     * 会员类型（0:非会员 1：会员）
     */
    private String disUserType;

    /** */
    private String isDelete;

    /** */
    private String updateTime;

    /** */
    private String addTime;

}