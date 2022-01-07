package cn.mrcode.kafka.collector.web;

import lombok.Data;
import lombok.ToString;

/**
 * @author mrcode
 * @date 2022/1/7 20:44
 */
@Data
@ToString
public class WatcherItem {
    private String title;
    private String executionTime;
    private String applicationName;
    private String level;
    private String body;
}
