#@formatter:off
CREATE TABLE IF NOT EXISTS `message` (
  `id`          BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `type`        TINYINT NOT NULL COMMENT '类型:1-评论,2-通知',
  `owner_uid`    BIGINT NOT NULL COMMENT '消息拥有者UID',
  `sender_uid`   BIGINT NOT NULL COMMENT '消息发送者UID',
  `comment_id`   BIGINT NOT NULL DEFAULT 0 COMMENT '消息对应的评论ID',
  `status`      TINYINT NOT NULL DEFAULT 0 COMMENT '状态: 0-未读，1-已读，2-删除',
  `data`        VARCHAR(500) NOT NULL COMMENT '消息实体',
  `create_time`  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
)
  ENGINE = INNODB
  DEFAULT CHARSET = utf8mb4;



