-- 记录消息日志数据的表接口
-- mysql5.7 不支持 timestamp 默认值为 0000-00-00 00:00:00，
-- 所以这里去掉默认值，并且改成 datetime 类型
CREATE TABLE
    IF
    NOT EXISTS `broker_message`
(
    `message_id`  VARCHAR(128) NOT NULL,
    `message`     VARCHAR(4000),
    `try_count`   INT(4)                DEFAULT 0,
    `status`      VARCHAR(10)           DEFAULT '',
    `next_retry`  datetime     NOT NULL,
    `create_time` datetime     not null,
    `update_time` datetime     NOT NULL,
    PRIMARY KEY (`message_id`)
    ) ENGINE = INNODB
    DEFAULT charset = utf8;