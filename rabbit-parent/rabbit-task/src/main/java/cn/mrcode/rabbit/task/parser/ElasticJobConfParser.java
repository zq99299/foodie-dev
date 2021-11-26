package cn.mrcode.rabbit.task.parser;

import cn.mrcode.rabbit.task.autoconfigure.JobZookeeperProperties;

/**
 * @author mrcode
 * @date 2021/11/25 21:30
 */
public class ElasticJobConfParser {
    private JobZookeeperProperties jobZookeeperProperties;

    public ElasticJobConfParser(JobZookeeperProperties jobZookeeperProperties) {
        this.jobZookeeperProperties = jobZookeeperProperties;
    }
}
