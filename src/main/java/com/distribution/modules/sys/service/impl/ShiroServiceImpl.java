package com.distribution.modules.sys.service.impl;

import com.distribution.common.utils.Constant;
import com.distribution.modules.sys.dao.SysMenuDao;
import com.distribution.modules.sys.dao.SysUserDao;
import com.distribution.modules.sys.entity.SysMenuEntity;
import com.distribution.modules.sys.entity.SysUserEntity;
import com.distribution.modules.sys.entity.SysUserTokenEntity;
import com.distribution.modules.sys.service.ShiroService;
import com.distribution.modules.sys.service.SysUserTokenService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ShiroServiceImpl implements ShiroService {
    @Autowired
    private SysMenuDao sysMenuDao;
    @Autowired
    private SysUserDao sysUserDao;
    @Autowired
    private SysUserTokenService sysUserTokenService;

    @Override
    public Set<String> getUserPermissions(long userId) {
        List<String> permsList;

        //系统管理员，拥有最高权限
        if (userId == Constant.SUPER_ADMIN) {
            List<SysMenuEntity> menuList = sysMenuDao.queryList(null);
            permsList = new ArrayList<>(menuList.size());
            menuList.stream().map(SysMenuEntity::getPerms).forEach(permsList::add);
        } else {
            permsList = sysUserDao.queryAllPerms(userId);
        }
        //用户权限列表
        Set<String> permsSet = new HashSet<>();
        permsList.stream().filter(StringUtils::isNotBlank).map(perms -> Arrays.asList(perms.trim().split(","))).forEachOrdered(permsSet::addAll);
        return permsSet;
    }

    @Override
    public SysUserTokenEntity queryByToken(String token) {
        return sysUserTokenService.queryByToken(token);
    }

    @Override
    public SysUserEntity queryUser(Long userId) {
        return sysUserDao.queryObject(userId);
    }
}
