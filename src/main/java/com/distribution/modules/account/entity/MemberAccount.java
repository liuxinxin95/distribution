package com.distribution.modules.account.entity;

import com.distribution.modules.dis.entity.DisMemberInfoEntity;
import lombok.Data;

import java.math.BigDecimal;

/**
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table member_account
 *
 * @mbg.generated do_not_delete_during_merge
 */
@Data
public class MemberAccount {
    /**
     * 主键ID
     */
    private String accountId;

    /**
     * 会员
     */
    private DisMemberInfoEntity member;

    /**
     * 账户类型
     */
    private String memberType;

    /** */
    private BigDecimal memberAmount;

    /** */
    private String isDelete;

    /** */
    private String addTime;

    /** */
    private String updateTime;

}