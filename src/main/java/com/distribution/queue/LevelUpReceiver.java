package com.distribution.queue;

import com.alibaba.fastjson.JSON;
import com.distribution.modules.dis.entity.DisMemberInfoEntity;
import com.distribution.modules.dis.service.DisMemberInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author ChunLiang Hu
 * @Company
 * @Project distribution
 * @Package com.distribution.queue
 * @Description TODO(描述)
 * @create 2018/5/12-10:31
 */
@Slf4j
@Component
@RabbitListener(queues = "level_up")
public class LevelUpReceiver {
    @Autowired
    private DisMemberInfoService disMemberInfoService;
    @RabbitHandler
    public void process(String msg) {
        if (StringUtils.isBlank(msg)) {
            log.error("消息为空");
            return;
        }
        DisMemberInfoEntity memberInfo = (DisMemberInfoEntity) JSON.parse(msg);
        disMemberInfoService.levelUp(memberInfo);
    }

}
