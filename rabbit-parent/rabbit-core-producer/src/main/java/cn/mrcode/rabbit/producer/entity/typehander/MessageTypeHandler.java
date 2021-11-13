package cn.mrcode.rabbit.producer.entity.typehander;

import cn.mrcode.rabbit.api.Message;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author mrcode
 * @date 2021/11/13 20:22
 */
@MappedTypes(value = {Message.class})
@MappedJdbcTypes(value = {JdbcType.VARCHAR})
public class MessageTypeHandler extends BaseTypeHandler<Message> {
    /**
     * 在生成 SQL 语句时，将对象转换为数据库中的 类型
     *
     * @param ps
     * @param i
     * @param parameter
     * @param jdbcType
     * @throws SQLException
     */
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Message parameter, JdbcType jdbcType) throws SQLException {
        if (parameter == null) {
            return;
        }
        ps.setString(i, JSON.toJSONString(parameter));
    }

    // 下面三个都是从结果集中转换成  实体类

    /**
     * 从数据库读取时，将 字段中的 字符串转换为实体类
     *
     * @param rs
     * @param columnName
     * @return
     * @throws SQLException
     */
    @Override
    public Message getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String val = rs.getString(columnName);
        return convert(val);
    }

    @Override
    public Message getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String val = rs.getString(columnIndex);
        return convert(val);
    }

    private Message convert(String val) {
        if (StringUtils.isBlank(val)) {
            return null;
        }
        return JSON.parseObject(val, Message.class);
    }

    @Override
    public Message getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String val = cs.getString(columnIndex);
        return JSON.parseObject(val, Message.class);
    }
}
