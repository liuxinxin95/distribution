package com.distribution.modules.sys.service.impl;

import com.distribution.common.utils.Result;
import com.distribution.modules.sys.dao.SysUserTokenDao;
import com.distribution.modules.sys.entity.SysUserTokenEntity;
import com.distribution.modules.sys.oauth2.TokenGenerator;
import com.distribution.modules.sys.service.SysUserTokenService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;


/**
 * @author huchunliang
 */
@Service("sysUserTokenService")
public class SysUserTokenServiceImpl implements SysUserTokenService {
    @Autowired
    private SysUserTokenDao sysUserTokenDao;
    @Autowired
    private RedisTemplate redisTemplate;
    /**
     * 12小时后过期
     */
    private final static int EXPIRE = 3600 * 12;

    @Override
    public SysUserTokenEntity queryByUserId(Long userId) {
        return sysUserTokenDao.queryByUserId(userId);
    }

    @Override
    public SysUserTokenEntity queryByToken(String token) {
        String id = (String) redisTemplate.opsForValue().get(token);
        if (StringUtils.isBlank(id)) {
            return new SysUserTokenEntity();
        }
        return queryByUserId(NumberUtils.toLong(id));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(SysUserTokenEntity token) throws Exception {
        sysUserTokenDao.save(token);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SysUserTokenEntity token) throws Exception {
        sysUserTokenDao.update(token);
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result createToken(long userId) throws Exception {
        //生成一个token
        String token = TokenGenerator.generateValue();

        //当前时间
        LocalDateTime now = LocalDateTime.now();
        //过期时间
        LocalDateTime expireTime = LocalDateTime.now().plusDays(1);

        //判断是否生成过token
        //从Redis中根据token获取用户ID
        String o = (String) redisTemplate.opsForValue().get(token);
//        SysUserTokenEntity tokenEntity = queryByUserId(userId);
        if (userId != NumberUtils.toLong(o)) {
            SysUserTokenEntity tokenEntity = new SysUserTokenEntity();
            tokenEntity.setUserId(userId);
            tokenEntity.setToken(token);
            tokenEntity.setUpdateTime(now);
            tokenEntity.setExpireTime(expireTime);
            //保存token
            //向Redis中存入token,12小时后过期
            ValueOperations operations = redisTemplate.opsForValue();
            operations.set(token, String.valueOf(userId), EXPIRE, TimeUnit.SECONDS);
            //向数据库中存入token
            save(tokenEntity);
//        } else {
//            SysUserTokenEntity tokenEntity = new SysUserTokenEntity();
//            tokenEntity.setToken(token);
//            tokenEntity.setUpdateTime(now);
//            tokenEntity.setExpireTime(expireTime);
//
//            //更新token
//            update(tokenEntity);
        }

        return Result.ok().put("token", token).put("expire", EXPIRE);
    }
}
