package cn.mrcode.rabbit.producer.mapper;

import cn.mrcode.rabbit.producer.entity.BrokerMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @author mrcode
 * @date 2021/11/1 22:03
 */
@Mapper
public interface BrokerMessageMapper {
    int deleteByPrimaryKey(String messageId);

    int insert(BrokerMessage record);

    int insertSelective(BrokerMessage record);

    BrokerMessage selectByPrimaryKey(String messageId);

    int updateByPrimaryKeySelective(BrokerMessage record);

    int updateByPrimaryKey(BrokerMessage record);

    void changeBrokerMessageStatus(@Param("messageId") String messageId, @Param("messageStatus") String messageStatus, @Param("updateTime") Date updateTime);

    List<BrokerMessage> queryBrokerMessageStatus4Timeout(@Param("messageStatus") String messageStatus);

    List<BrokerMessage> queryBrokerMessageStatus(@Param("messageStatus") String messageStatus);

    int update4TryCount(@Param("messageId") String messageId, @Param("updateTime") Date updateTime);
}
