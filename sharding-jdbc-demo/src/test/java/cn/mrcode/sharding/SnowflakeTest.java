package cn.mrcode.sharding;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;

/**
 * @author mrcode
 * @date 2022/2/4 12:58
 */
public class SnowflakeTest {
    public static void main(String[] args) {
        //参数1为终端ID
//参数2为数据中心ID
        Snowflake snowflake = IdUtil.getSnowflake(1, 1);
        for (int i = 0; i < 10; i++) {
            long id = snowflake.nextId();
            System.out.println(id);
        }
    }
}
