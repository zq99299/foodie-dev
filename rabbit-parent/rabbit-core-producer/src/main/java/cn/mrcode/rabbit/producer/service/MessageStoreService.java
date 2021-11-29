package cn.mrcode.rabbit.producer.service;

import cn.mrcode.rabbit.producer.constant.BrokerMessageStatus;
import cn.mrcode.rabbit.producer.entity.BrokerMessage;
import cn.mrcode.rabbit.producer.mapper.BrokerMessageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author mrcode
 * @date 2021/11/11 20:39
 */
@Service
public class MessageStoreService {
    @Autowired
    private BrokerMessageMapper brokerMessageMapper;

    /**
     * 往消息日志表中插入一条数据
     *
     * @param brokerMessage
     */
    public void insert(BrokerMessage brokerMessage) {
        brokerMessageMapper.insert(brokerMessage);
    }

    /**
     * 消息发送成功
     *
     * @param messageId
     */
    public void success(String messageId) {
        brokerMessageMapper.changeBrokerMessageStatus(
                messageId,
                BrokerMessageStatus.SEND_OK.getCode(),
                new Date());
    }


    /**
     * 消息发送失败
     *
     * @param messageId
     */
    public void fail(String messageId) {
        brokerMessageMapper.changeBrokerMessageStatus(
                messageId,
                BrokerMessageStatus.SEND_FALL.getCode(),
                new Date());
    }

    /**
     * 根据状态抓取已经超时的数据
     *
     * @param status
     * @return
     */
    public List<BrokerMessage> fetchTimeOutMessage4Retry(BrokerMessageStatus status) {
        return brokerMessageMapper.queryBrokerMessageStatus4Timeout(status.getCode());
    }

    /**
     * 将此消息的重试次数加 1
     *
     * @param messageId
     */
    public void updateTryCount(String messageId) {
        brokerMessageMapper.update4TryCount(messageId, new Date());
    }

    /**
     * 按消息 ID 查询消息日志
     *
     * @param messageId
     * @return
     */
    public BrokerMessage selectByMessageId(String messageId) {
        return brokerMessageMapper.selectByPrimaryKey(messageId);
    }
}
