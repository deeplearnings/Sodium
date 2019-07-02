package net.onebean.saas.portal.consumer.mq;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.eakay.common.exception.BusinessException;
import com.eakay.common.model.BaseResponse;
import com.eakay.core.form.Parse;
import net.onebean.saas.portal.common.error.ErrorCodesEnum;
import net.onebean.saas.portal.model.TenantInfo;
import net.onebean.saas.portal.service.TenantInfoService;
import com.eakay.tenant.mngt.api.model.ModifyTtenantInfoBatchSyncFlagReq;
import com.eakay.tenant.mngt.api.model.TenantInfoStatusEnum;
import com.eakay.tenant.mngt.api.model.TenantInfoSyncVo;
import com.eakay.tenant.mngt.api.service.TennantInfoApi;
import com.eakay.util.CollectionUtil;
import com.eakay.util.StringUtils;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class DevopsSyncTenantAccountComsumer {


    private final static Logger logger = LoggerFactory.getLogger(DevopsSyncTenantAccountComsumer.class);
    private final static String TRUE_VALUE = "1";

    @Autowired
    private TenantInfoService tenantInfoService;
    @Autowired
    private TennantInfoApi  tennantInfoApi;



    @RabbitListener(queues = "eakay.devops.tenant.info.sync.tenant.account")
    @RabbitHandler
    public void process(List<TenantInfoSyncVo> unSync, Channel channel, Message message) {
        logger.info("DevopsInitAccountConsumer process access  text = "+ JSON.toJSONString(unSync, SerializerFeature.WriteMapNullValue));
        List<TenantInfo> normal = new ArrayList<>();
        List<Long> normalIds = new ArrayList<>();
        List<Long> deletedTenantIds = new ArrayList<>();
        StringBuilder syncIds = new StringBuilder();
        try {
            for (TenantInfoSyncVo v : unSync) {
                String tenantName = Optional.of(v).map(TenantInfoSyncVo::getTenantName).orElse(null);
                String tenantId = Optional.of(v).map(TenantInfoSyncVo::getId).orElse(null);
                syncIds.append(tenantId).append(",");
                if(v.getStatus().equals(TenantInfoStatusEnum.NORMAL.getKey()) || v.getIsDeleted().equals(TRUE_VALUE)){
                    if (StringUtils.isEmpty(tenantName) || StringUtils.isEmpty(tenantId)){
                        throw new BusinessException(ErrorCodesEnum.CLOUD_API_RESP_ERR.code(), ErrorCodesEnum.CLOUD_API_RESP_ERR.msg()+" tenantName or tenantId is empty");
                    }
                    TenantInfo tenantInfo = new TenantInfo();
                    tenantInfo.setTenantName(tenantName);
                    tenantInfo.setTenantId(Parse.toInt(tenantId));
                    normalIds.add(Parse.toLong(tenantId));
                    normal.add(tenantInfo);
                }else{
                    deletedTenantIds.add(Parse.toLong(tenantId));
                }
            }
            ModifyTtenantInfoBatchSyncFlagReq  req = new ModifyTtenantInfoBatchSyncFlagReq();
            req.setIds(syncIds.toString());
            req.setIsSync(TRUE_VALUE);

            if (CollectionUtil.isNotEmpty(normalIds)){
                tenantInfoService.deleteByTenantIds(normalIds);
            }

            tenantInfoService.saveBatch(normal);

            if (CollectionUtil.isNotEmpty(deletedTenantIds)){
                tenantInfoService.deleteByTenantIds(deletedTenantIds);
            }

            if (StringUtils.isNotEmpty(req.getIds())){
                BaseResponse response = tennantInfoApi.updateBatchSyncFlag(req);
                String errCode = Optional.ofNullable(response).map(BaseResponse::getErrCode).orElse(null);
                if (StringUtils.isEmpty(errCode) || !errCode.equals(ErrorCodesEnum.SUSSESS.code())){
                    throw new BusinessException(ErrorCodesEnum.CLOUD_API_RESP_ERR.code(), ErrorCodesEnum.CLOUD_API_RESP_ERR.msg()+" api of tennantInfoApi.update");
                }
            }

            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        } catch (Exception e) {
            logger.error("process get a error = ",e);
            throw new BusinessException(ErrorCodesEnum.RABBIT_MQ_BUSSINES_ERR.code(), ErrorCodesEnum.RABBIT_MQ_BUSSINES_ERR.msg());
        }
    }
}