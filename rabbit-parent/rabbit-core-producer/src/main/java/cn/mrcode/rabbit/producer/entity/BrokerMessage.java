package cn.mrcode.rabbit.producer.entity;

import cn.mrcode.rabbit.api.Message;
import org.apache.ibatis.type.MappedTypes;

import java.io.Serializable;
import java.util.Date;

/**
 * 消息记录实体映射
 *
 * @author mrcode
 * @date 2021/11/1 22:01
 */
public class BrokerMessage implements Serializable {
    private String messageId;
    private Message message;
    private Integer tryCount = 0;
    private String status;
    private Date nextRetry;
    private Date createTime;
    private Date updateTime;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public Integer getTryCount() {
        return tryCount;
    }

    public void setTryCount(Integer tryCount) {
        this.tryCount = tryCount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getNextRetry() {
        return nextRetry;
    }

    public void setNextRetry(Date nextRetry) {
        this.nextRetry = nextRetry;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
