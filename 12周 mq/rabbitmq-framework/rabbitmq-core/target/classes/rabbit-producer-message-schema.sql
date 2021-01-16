CREATE TABLE IF NOT EXISTS `broker_message`
(
    `message_id`  varchar(128) NOT NULL,
    `message`     varchar(4000),
    `try_count`   int(4)                DEFAULT 0,
    `status`      varchar(10)           DEFAULT '',
    `next_retry`  timestamp    NOT NULL DEFAULT '1971-01-01 00:00:00',
    `create_time` timestamp    NOT NULL DEFAULT '1971-01-01 00:00:00',
    `update_time` timestamp    NOT NULL DEFAULT '1971-01-01 00:00:00',
    PRIMARY KEY (`message_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8MB4;